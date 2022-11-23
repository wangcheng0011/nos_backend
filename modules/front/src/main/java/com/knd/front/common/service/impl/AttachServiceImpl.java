package com.knd.front.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.StringUtils;
import com.knd.common.obs.ObsObjectUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.common.service.AttachService;
import com.knd.front.entity.Attach;
import com.knd.front.entity.UserDetail;
import com.knd.front.login.mapper.UserDetailMapper;
import com.knd.front.pay.dto.ImgDto;
import com.knd.front.train.mapper.AttachMapper;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author Lenovo
 */
@Service
@Transactional
@Slf4j
public class AttachServiceImpl implements AttachService {
    //终端，可以不带协议、也可以带协议
    @Value("${OBS.endPoint}")
    private String endPoint;
    //公钥
    @Value("${OBS.ak}")
    private String ak;
    //私钥
    @Value("${OBS.sk}")
    private String sk;
    //桶名
    @Value("${OBS.bucketname}")
    private String bucketname;

    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    //图片文件夹路径
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;

    @Autowired
    private AttachMapper attachMapper;
    @Autowired
    private UserDetailMapper userDetailMapper;

    @Override
    public Attach saveAttach(String userId, String picAttachName, String picAttachNewName, String picAttachSize) {
        log.info("---------------保存附件开始----------------");
        log.info("saveAttach userId:{{}}",userId);
        log.info("saveAttach picAttachName:{{}}",picAttachName);
        log.info("saveAttach picAttachNewName:{{}}",picAttachNewName);
        log.info("saveAttach picAttachSize:{{}}",picAttachSize);
        //存储文件信息
        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setEndPoint(endPoint);
        log.info("saveAttach endPoint:{{}}",endPoint);
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, config);
        log.info("saveAttach ak:{{}}",ak);
        // URL有效期，1年
        long expireSeconds =  365 * 24 * 3600L;
        Attach aPi = null;

        if (StringUtils.isNotEmpty(picAttachNewName)) {
            //获取图片访问的路径
            log.info("saveAttach obsClient:{{}}",obsClient);
            log.info("saveAttach picAttachNewName:{{}}",picAttachNewName);
            log.info("saveAttach bucketname:{{}}",bucketname);
            log.info("saveAttach expireSeconds:{{}}",expireSeconds);
            String picUrl = ObsObjectUtil.getUrl(obsClient, picAttachNewName, bucketname, expireSeconds);
            log.info("saveAttach picUrl:{{}}",picUrl);
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
            log.info("saveAttach aPi:{{}}",aPi);
            attachMapper.insert(aPi);
        }
        log.info("---------------保存附件结束----------------");
        return aPi;
    }

    //根据文件id获取文件信息
    @Override
    public Attach getInfoById(String id) {
        QueryWrapper<Attach> qw = new QueryWrapper<>();
        qw.eq("deleted", "0");
        qw.select("fileName", "filePath", "fileSize", "fileType");
        qw.eq("id", id);
        return attachMapper.selectOne(qw);
    }

    @Override
    public String getHeadPicUrl(String userId){
        UserDetail userDetail = userDetailMapper.selectOne(new QueryWrapper<UserDetail>().eq("userId", userId).eq("deleted", "0"));
        if(userDetail!=null){
            Attach attach = attachMapper.selectById(userDetail.getHeadPicUrlId());
            return attach!=null ? fileImagesPath+attach.getFilePath() : "";
        }else{
            return "";
        }
    }

    //将原文件标识设为删除
    @Override
    public void deleteFile(String id,String userid) {
        Attach a = attachMapper.selectById(id);
        if(StringUtils.isNotEmpty(a)){
            a.setId(id);
            a.setDeleted("1");
            a.setLastModifiedDate(LocalDateTime.now());
            a.setLastModifiedBy(userid);
            attachMapper.updateById(a);
        }
    }

    @Override
    public ImgDto getImgDto(String urlId) {
        //根据id获取图片信息
        Attach aPi = getInfoById(urlId);
        ImgDto imgDto = new ImgDto();
        if (aPi != null) {
            imgDto.setPicAttachUrl(fileImagesPath + aPi.getFilePath());
            imgDto.setPicAttachSize(aPi.getFileSize());
            String[] strs = (aPi.getFilePath()).split("\\?");
            imgDto.setPicAttachNewName(imageFoldername + strs[0]);
            imgDto.setPicAttachName(aPi.getFileName());
        }
        return imgDto;
    }
}
