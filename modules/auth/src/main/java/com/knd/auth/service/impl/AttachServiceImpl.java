package com.knd.auth.service.impl;

import com.knd.auth.entity.Attach;
import com.knd.auth.mapper.AttachMapper;
import com.knd.auth.service.AttachService;
import com.knd.common.basic.StringUtils;
import com.knd.common.obs.ObsObjectUtil;
import com.knd.common.uuid.UUIDUtil;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
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

    @Autowired
    private AttachMapper attachMapper;

    @Override
    public Attach saveAttach(String userId, String picAttachName, String picAttachNewName, String picAttachSize) {
        log.info("--------------------------------------保存附件开始----------------------------------------");
        log.info("saveAttach userId：{{}}",userId);
        log.info("saveAttach picAttachName：{{}}",picAttachName);
        log.info("saveAttach picAttachNewName：{{}}",picAttachNewName);
        log.info("saveAttach picAttachSize：{{}}",picAttachSize);
        //存储文件信息
        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setEndPoint(endPoint);
        log.info("saveAttach config:{}",config);
        // 创建ObsClient实例
        log.info("saveAttach ak:{}",ak);
        log.info("saveAttach sk:{}",sk);
        ObsClient obsClient = new ObsClient(ak, sk, config);
        // URL有效期，1年
        long expireSeconds =  365 * 24 * 3600L;
        Attach aPi = null;
        if (StringUtils.isNotEmpty(picAttachNewName)) {
            //获取图片访问的路径
            String picUrl = ObsObjectUtil.getUrl(obsClient, picAttachNewName, bucketname, expireSeconds);
            log.info("saveAttach picUrl:{}",picUrl);
            String[] strs2 = picUrl.split("\\/");
            picUrl = strs2[strs2.length - 1];
            log.info("saveAttach picUrl:{}",picUrl);
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
            attachMapper.insert(aPi);
        }
        log.info("saveAttach aPi:{}",aPi);
        log.info("-----------------------保存附件结束-----------------------------");
        return aPi;
    }
}
