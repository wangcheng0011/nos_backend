package com.knd.common.obs;


import com.obs.services.ObsClient;
import com.obs.services.model.ObsBucket;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObsBucketUtil {

    /**
     * 判断桶是否存在
     */
    public static boolean isExist(ObsClient obsClient, String bucketName) {
        boolean hasbucket;
        try {
            hasbucket = obsClient.headBucket(bucketName);
        } catch (Exception e) {
            hasbucket = false;
        }
        return hasbucket;
    }


    /**
     * 创建桶
     */
    public static boolean createBucket(ObsClient obsClient, String bucketName, String location) {
        try {
            //创建桶-带参数
            ObsBucket obsBucket = new ObsBucket();
            //桶名
            obsBucket.setBucketName(bucketName);
//        // 设置桶访问权限为公共读，默认是私有读写
//                obsBucket.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
//        // 设置桶的存储类型为归档存储
//                obsBucket.setBucketStorageClass(StorageClassEnum.COLD);
            // 设置桶区域位置
            //华东-上海一 cn-east-3
            obsBucket.setLocation(location);
            // 创建桶成功
            obsClient.createBucket(obsBucket);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return false;
        }
    }


}
