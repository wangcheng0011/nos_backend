package com.knd.manage.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.obs.ObsObjectUtil;
import com.knd.common.obs.ObsSecurityUtil;
import com.knd.common.obs.UploadAttachToOBS;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.common.dto.UploadInfoDto;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.common.vo.VoUploadInfo;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-10
 */
@Service
@Slf4j
public class AttachServiceImpl extends ServiceImpl<AttachMapper, Attach> implements IAttachService {
    //终端，可以不带协议、也可以带协议
    @Value("${OBS.endPoint}")
    private String endPoint;
    //公钥
    @Value("${OBS.ak}")
    private String ak;
    //私钥
    @Value("${OBS.sk}")
    private String sk;
    //图片文件夹路径
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;
    //视频文件夹路径
    @Value("${OBS.videoFoldername}")
    private String videoFoldername;
    //apk文件夹路径
    @Value("${OBS.appFoldername}")
    private String appFoldername;
    //桶名
    @Value("${OBS.bucketname}")
    private String bucketname;


    @Override
    public Attach insertReturnEntity(Attach entity) {
        return null;
    }

    @Override
    public Attach updateReturnEntity(Attach entity) {
        return null;
    }

    //根据文件id获取文件信息
    @Override
    public Attach getInfoById(String id) {
        QueryWrapper<Attach> qw = new QueryWrapper<>();
        qw.eq("deleted", "0");
        qw.select("fileName", "filePath", "fileSize", "fileType");
        qw.eq("id", id);
        return baseMapper.selectOne(qw);
    }

    //上传文件
    @Override
    public Result uploadAttach(MultipartFile file, String folderType) {

        try {
            //获取原文件名
            String fileName = file.getOriginalFilename();
            //获取后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
            //新文件名【带后缀】
            String newFileName = UUIDUtil.getShortUUID() + "." + suffixName;
            log.info("==============看到我吗，开始上传文件，原名字是 : " + fileName + " ,新名字 : " + newFileName);
            /**
             * 将文件存储到obs里面
             */
            //文件夹
            String foldername;
            //不同类型的附件放在不同的桶里面
            if ("10".equals(folderType)) {
                foldername = videoFoldername;
            } else if ("20".equals(folderType)) {
                foldername = imageFoldername;
            } else if ("30".equals(folderType)) {
                foldername = appFoldername;
            } else {
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            //将文件存储到obs里面
            String getUrl = UploadAttachToOBS.uploadAttachToOBS(ak, sk, endPoint, file, bucketname, newFileName, foldername);
            log.info("==============看到我吗，上传文件上传结束 ");
            log.info("==============存储的相对路径：" + getUrl);
            //将文件信息存储到数据库
            Attach attach = new Attach();
            String uid = UUIDUtil.getShortUUID();
            attach.setId(uid);
            attach.setFileName(fileName);
            //将访问obs的get路径存放在数据库里面
            attach.setFilePath(getUrl);
            attach.setFileSize(file.getSize() + "");
            attach.setFileType(suffixName);
            String userid = UserUtils.getUserId();
            attach.setCreateBy(userid);
            attach.setCreateDate(LocalDateTime.now());
            attach.setLastModifiedBy(userid);
            attach.setLastModifiedDate(LocalDateTime.now());
            attach.setDeleted("0");
            baseMapper.insert(attach);
            log.info("==============看到我吗，数据库存储结束 ");
            return ResultUtil.success(uid);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("异常：" + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultUtil.error(ResultEnum.FAIL);
        }
    }

    //获取obs上传信息
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result getUploadInfo(VoUploadInfo vo) throws Exception {
        UploadInfoDto u = new UploadInfoDto();
        u.setAccessKeyId(ak);
        u.setBucketname(bucketname);
        u.setEndPoint(endPoint);
        //对象名称
        String key;
        if (vo.getFolderType().endsWith("10")) {
            //视频
            key = videoFoldername + vo.getNewName();
            u.setContentType("视频");
        } else if (vo.getFolderType().endsWith("20")) {
            //图片
            key = imageFoldername + vo.getNewName();
            u.setContentType("图片");
        } else {
            //apk
            key = appFoldername + vo.getNewName();
            u.setContentType("apk");
        }
        u.setKey(key);
        ObsSecurityUtil ob = new ObsSecurityUtil();
        /**
         * 获取安全策略描述
         */
        //有效时间,1分钟[UTC时间]
        String expiration = ob.formateDateForUTC(System.currentTimeMillis() + 60000);
        Map<String, String[]> conditions = new HashMap<>();
        //文件权限属性：私读
        conditions.put("x-obs-acl", new String[]{"private"});
        //桶名
        conditions.put("bucket", new String[]{bucketname});
        //对象名称
        conditions.put("key", new String[]{key});
        //0kb-500mb之间
        conditions.put("content-length-range", new String[]{"content-length-range", "0", "524288000"});
        u.setPolicy(ob.getPolicy(sk, conditions, expiration));
        /**
         * 获取数字签名
         */
        u.setESignature(ob.getSignature(u.getPolicy()));
        return ResultUtil.success(u);
    }

    //将原文件标识设为删除
    @Override
    public void deleteFile(String id,String userid) {
        Attach a = new Attach();
        a.setId(id);
        a.setDeleted("1");
        a.setLastModifiedDate(LocalDateTime.now());
        a.setLastModifiedBy(userid);
        baseMapper.updateById(a);
    }

    @Override
    public Attach saveAttach(String userId, String picAttachName, String picAttachNewName, String picAttachSize) {
        //存储文件信息
        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setEndPoint(endPoint);
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, config);
        // URL有效期，1年
        long expireSeconds =  365 * 24 * 3600L;
        Attach aPi = null;
        if (StringUtils.isNotEmpty(picAttachNewName)) {
            //获取图片访问的路径
            String picUrl = ObsObjectUtil.getUrl(obsClient, picAttachNewName, bucketname, expireSeconds);
            String[] strs2 = picUrl.split("\\/");
            picUrl = strs2[strs2.length - 1];
            //存储图片信息
            aPi = new Attach();
            aPi.setId(UUIDUtil.getShortUUID());
            aPi.setFileName(picAttachNewName);
            aPi.setFilePath(picUrl);
            aPi.setFileSize(picAttachSize);
            aPi.setFileType(picAttachName.substring(picAttachName.lastIndexOf(".") + 1));
            aPi.setCreateDate(LocalDateTime.now());
            aPi.setCreateBy(userId);
            aPi.setLastModifiedDate(LocalDateTime.now());
            aPi.setLastModifiedBy(userId);
            aPi.setDeleted("0");
            baseMapper.insert(aPi);
        }
        return aPi;
    }


}
