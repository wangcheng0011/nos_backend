package com.knd.batch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.batch.entity.Attach;
import com.knd.batch.mapper.AttachMapper;
import com.knd.batch.service.IAttachService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.obs.ObsObjectUtil;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-10-09
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AttachServiceImpl extends ServiceImpl<AttachMapper, Attach> implements IAttachService {

    @Value("${upload.imagesTypeList}")
    private String[] imagesTypeList;

    @Value("${upload.videoTypeList}")
    private String[] videoTypeList;

    @Value("${upload.appTypeList}")
    private String[] appTypeList;

    @Value("${OBS.imageFoldername}")
    private String im;

    @Value("${OBS.videoFoldername}")
    private String vi;

    @Value("${OBS.appFoldername}")
    private String ap;

    @Value("${OBS.obsPrefix}")
    private String obsPrefix;

    @Value("${OBS.bucketname}")
    private String bucketname;

    @Value("${OBS.endPoint}")
    private String endPoint;

    @Value("${OBS.ak}")
    private String ak;

    @Value("${OBS.sk}")
    private String sk;

    @Override
    public Attach insertReturnEntity(Attach entity) {
        return null;
    }

    @Override
    public Attach updateReturnEntity(Attach entity) {
        return null;
    }

    //批量删除数据
    @Override
    public void deleteObsFile() {
        try {
            log.info("----------------------------批量删除数据开始---------------------------------");
            //从数据库获取标识为0的文件
            QueryWrapper<Attach> a = new QueryWrapper<>();
            a.select("id", "filePath", "fileType");
            a.eq("deleted", "0");
            List<Attach> la = baseMapper.selectList(a);
            log.info("deleteObsFile List<Attach> la:{{}}",la);
            //解析成集合
            List<String> ldb = new ArrayList<>();
            for (Attach c : la) {
                //前缀
                String type = "";
                if (c.getFileType().equalsIgnoreCase("mp4")) {
                    type = vi;
                } else if (c.getFileType().equalsIgnoreCase("png")
                        || c.getFileType().equalsIgnoreCase("jpg")) {
                    type = im;
                } else if (c.getFileType().equalsIgnoreCase("apk")) {
                    type = ap;
                } else {
                    log.info("文件id=" + c.getId() + "类型错误，该文件不操作");
                    continue;
                }
                ldb.add(type + (c.getFilePath().split("\\?"))[0]);
            }
            log.info("deleteObsFile List<String> ldb :{{}}",ldb);
            //需要保留的对象【文件夹前缀】
            ldb.add(obsPrefix);
            ldb.add(vi);
            ldb.add(im);
            ldb.add(ap);

            // 创建ObsClient实例
            ObsConfiguration config = new ObsConfiguration();
            config.setSocketTimeout(30000);
            config.setConnectionTimeout(10000);
            config.setEndPoint(endPoint);
            ObsClient obsClient = new ObsClient(ak, sk, config);
            //从obs获取指定桶里的所有指定一级前缀的对象名称
            List<String> lobs = ObsObjectUtil.getAllObjects(obsClient, bucketname, obsPrefix);
            //差集
            lobs.removeAll(ldb);
            log.info("deleteObsFile List<String> lobs :{{}}",lobs);
            //批量删除对象
            ObsObjectUtil.deleteMuch(obsClient, lobs, bucketname);
            log.info("----------------------------批量删除数据结束---------------------------------");
        } catch (Exception e) {
            log.info("===============================批量删除obs的文件【单线程】==================================\n"
                    + "失败，原因：\n"
                    + e.getMessage()
                    + "\n"
                    + "===============================批量删除obs的文件【单线程】==================================");
        }
    }

    //批量刷新访问路径
    @Override
    public void refreshUrl() {
        try {
            //数据库获取所有数据
            QueryWrapper<Attach> a = new QueryWrapper<>();
            a.select("id", "filePath", "fileType");
            a.eq("deleted", "0");
            List<Attach> la = baseMapper.selectList(a);
            //解析文件对象名称
            List<Attach> dbLa = new ArrayList<>();
            for (Attach c : la) {
                //前缀
                String type = "";
                if (c.getFileType().equalsIgnoreCase("mp4")) {
                    type = vi;
                } else if (c.getFileType().equalsIgnoreCase("png")
                        || c.getFileType().equalsIgnoreCase("jpg")) {
                    type = im;
                } else if (c.getFileType().equalsIgnoreCase("apk")) {
                    type = ap;
                } else {
                    log.info("文件id=" + c.getId() + "类型错误，该文件不操作");
                    continue;
                }
                c.setFilePath(type + (c.getFilePath().split("\\?"))[0]);
                dbLa.add(c);
            }
            if (!dbLa.isEmpty()) {
                // URL有效期，当前时间的后1年之内 ,单位秒
                long expireSeconds = (new Date()).getTime() / 1000 +  365 * 24 * 3600L;
                //分别获取新的url
                for (Attach t : dbLa) {
                    /*
                    //从obs获取文件的访问路径
                    String url = ObsObjectUtil.getUrl2(obsClient, t.getFilePath(), bucketname, expireSeconds);
                     */
                    //
                    //本地是生成签名后拼接路径
                    String url = ObsObjectUtil.getUrl3(ak, sk, bucketname, t.getFilePath(), expireSeconds);
                    if (url.equals("")) {
                        //文件不存在获取url失败
                        log.info("文件id= " + t.getId() + " 文件不存在获取url失败");
                        continue;
                    }
//                    System.out.println("https://"+bucketname+".obs.cn-east-3.myhuaweicloud.com/" + url);
                    //更新
                    String[] strs11 = url.split("\\/");
                    t.setFilePath(strs11[strs11.length - 1]);
                    t.setLastModifiedDate(LocalDateTime.now());
                    t.setLastModifiedBy("批处理刷新访问路径");
                }
                //批量修改数据库
                this.updateBatchById(dbLa);
            }
        } catch (Exception e) {
            log.info("===============================批量刷新obs访问路径【单线程】==================================\n"
                    + "失败，原因：\n"
                    + e.getMessage()
                    + "\n"
                    + "===============================批量刷新访问obs路径【单线程】==================================");
        }

    }


}
