package com.knd.common.obs;

import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.obs.services.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * //文件上传到华为云OBS
 */
@Slf4j
public class UploadAttachToOBS {

//    /**
//     * @param ak         公钥
//     * @param sk         私钥
//     * @param endPoint   终端
//     * @param file       文件对象
//     * @param bucketname 桶名
//     * @param fileName   文件名称
//     */
//    public static String uploadAttachToOBS2(String ak, String sk, String endPoint, MultipartFile file,
//                                           String bucketname, String fileName) throws IOException {
//        ObsConfiguration config = new ObsConfiguration();
//        config.setSocketTimeout(30000);
//        config.setConnectionTimeout(10000);
//        config.setEndPoint(endPoint);
//        log.info("==========创建连接");
//        // 创建ObsClient实例
//        ObsClient obsClient = new ObsClient(ak, sk, config);
//        log.info("==========创建成功");
//        log.info("==========准备上传文件");
//        //上传对象
//        obsClient.putObject(bucketname, fileName, new ByteArrayInputStream(file.getBytes()));
//        log.info("==========文件上传成功");
//        //获取可以适用于get访问该文件的url路径
//        // URL有效期，5年
//        long expireSeconds = 5 * 365 * 24 * 3600L;
//        TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expireSeconds);
//        //桶名
//        request.setBucketName(bucketname);
//        //这个文件的名称
//        request.setObjectKey(fileName);
//        //获取get访问路径
//        TemporarySignatureResponse responseUrl = obsClient.createTemporarySignature(request);
//        String url = responseUrl.getSignedUrl();
//        // 关闭obsClient
//        obsClient.close();
//        //解析路径，获取相对路径
//        String[] strs = url.split("\\/");
//        log.info("==============获取的访问全路径是：" + url);
//        return strs[strs.length - 1];
//    }

    /**
     * @param ak         公钥
     * @param sk         私钥
     * @param endPoint   终端
     * @param file       文件对象
     * @param bucketname 桶名
     * @param fileName   文件名称
     * @param foldername 文件夹名称
     */
    public static String uploadAttachToOBS(String ak, String sk, String endPoint, MultipartFile file,
                                           String bucketname, String fileName, String foldername) throws IOException {
        ObsConfiguration config = new ObsConfiguration();
        config.setSocketTimeout(30000);
        config.setConnectionTimeout(10000);
        config.setEndPoint(endPoint);
        log.info("==========创建连接");
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, config);
        log.info("==========创建成功");
        //
        //这里不做桶和文件夹是否存在的判断，会牺牲速度
        //
        log.info("==========准备上传文件到" + foldername + "文件里");
        //上传对象
        obsClient.putObject(bucketname, foldername + fileName, new ByteArrayInputStream(file.getBytes()));
        log.info("==========文件上传成功");
        //获取可以适用于get访问该文件的url路径
        // URL有效期，5年
        long expireSeconds = 5 * 365 * 24 * 3600L;
        TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expireSeconds);
        //桶名
        request.setBucketName(bucketname);
        //这个文件的名称
        request.setObjectKey(foldername + fileName);
        //获取get访问路径
        TemporarySignatureResponse responseUrl = obsClient.createTemporarySignature(request);
        String url = responseUrl.getSignedUrl();
        // 关闭obsClient
        obsClient.close();
        //解析路径，获取相对路径
        String[] strs = url.split("\\/");
        log.info("==============获取的访问全路径是：" + url);
        return strs[strs.length - 1];
    }


}
