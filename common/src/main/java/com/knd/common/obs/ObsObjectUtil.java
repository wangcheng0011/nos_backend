package com.knd.common.obs;

import com.knd.common.response.CustomResultException;
import com.knd.common.response.ResultEnum;
import com.obs.services.ObsClient;
import com.obs.services.model.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class ObsObjectUtil {
    /**
     * 判断文件是否存在
     */
    public static boolean isExist(ObsClient obsClient, String fileName, String bucketName) {
        try {
            //找不到对象会报异常 com.obs.services.internal.ServiceException
            ObjectMetadata metadata = obsClient.getObjectMetadata(bucketName, fileName);
            //列举办法来判断
//        ObjectListing result = obsClient.listObjects(bucketName);
//        for (ObsObject obsObject : result.getObjects()) {
////            System.out.println("=================================");
//            //文件名称【包括前缀】
////            System.out.println("\t" + obsObject.getObjectKey());
//            //id信息等数据
////            System.out.println("\t" + obsObject.getOwner());
//            if (fileName.equals(obsObject.getObjectKey())) {
//                return true;
//            }
//        }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * 获取指定桶里的所有对象名称
     */
    public static List<String> getAllObjects(ObsClient obsClient, String bucketName, String Prefix) {
        List<String> list = new ArrayList<>();
        ListObjectsRequest request = new ListObjectsRequest(bucketName);
        //获取指定一级前缀的文件夹和对象 名
        request.setPrefix(Prefix);
        //最大1000条每次
        request.setMaxKeys(1000);
        //
        ObjectListing result;
        //分页获取全部对象名称
        do {
            result = obsClient.listObjects(request);
            for (ObsObject obsObject : result.getObjects()) {
                list.add(obsObject.getObjectKey());
            }
            request.setMarker(result.getNextMarker());
        } while (result.isTruncated());

        return list;
    }


    /**
     * 获取文件的访问路径
     */
    public static String getUrl(ObsClient obsClient, String fileName, String bucketName, long expireSeconds) {
        try {
            //检查文件是否存在obs里面
            if (!ObsObjectUtil.isExist(obsClient, fileName, bucketName)) {
                //不存在
//                System.out.println("不存在");
                //obs找不到对象,抛出异常，回滚操作
                throw new CustomResultException(ResultEnum.FILE_OBS_URL_ERROR);
            }
            TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expireSeconds);
            log.info("getUrl request：{{}}",request);
            System.out.println(request);
            //桶名
            request.setBucketName(bucketName);
            //这个文件的名称
            request.setObjectKey(fileName);
            //获取get访问路径
            TemporarySignatureResponse responseUrl = obsClient.createTemporarySignature(request);
            log.info("访问路径是：" + responseUrl.getSignedUrl());
            return responseUrl.getSignedUrl();
        } catch (Exception e) {
            //obs找不到对象,抛出异常，回滚操作
            throw new CustomResultException(ResultEnum.FILE_OBS_URL_ERROR);
        }
    }

    /**
     * 从obs获取文件的访问路径-不抛异常
     */
    public static String getUrl2(ObsClient obsClient, String fileName, String bucketName, long expireSeconds) {
        try {
            //检查文件是否存在obs里面
            if (!ObsObjectUtil.isExist(obsClient, fileName, bucketName)) {
                //不存在
                //obs找不到对象
                return "";
            }
            TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, expireSeconds);
            System.out.println(request);
            //桶名
            request.setBucketName(bucketName);
            //这个文件的名称
            request.setObjectKey(fileName);
            //获取get访问路径
            TemporarySignatureResponse responseUrl = obsClient.createTemporarySignature(request);
//            log.info("访问路径是：" + responseUrl.getSignedUrl());
            return responseUrl.getSignedUrl();
        } catch (Exception e) {
            //obs找不到对象
            return "";
        }
    }

    /**
     * 本地是生成签名后拼接路径
     */
    public static String getUrl3(String ak, String sk,  String bucketName, String fileName,long expireSeconds) {
        try {
            //获取加密好的签名
            SignatureUtil s = new SignatureUtil();
            //拼接路径
            return fileName + "?AccessKeyId=" + ak + "&Expires=" + expireSeconds
                    + "&Signature=" + s.getSignature(ak, sk, bucketName,fileName, expireSeconds);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 批量删除文件 -- 每次最多删除1000个对象
     */
    public static void deleteMuch(ObsClient obsClient, List<String> fileNames, String bucketName) {
        List<List<String>> ltimes = new ArrayList<>();

        int count = fileNames.size();
        if (count < 1) {
            return;
        } else if (count <= 1000) {
            ltimes.add(fileNames);
        } else {
            //大于1000条
            int yu = count % 1000;
            //满1000条的段数
            int page = count / 1000;
            //每段最大截取1000条数据
            for (int i = 0; i < page; i++) {
                List<String> d = new ArrayList<>();
                d = fileNames.subList(i * 1000, (i + 1) * 1000);
                ltimes.add(d);
            }
            if (yu != 0) {
                List<String> d = new ArrayList<>();
                //有余数
                d = fileNames.subList((page) * 1000, count);
                ltimes.add(d);
            }
        }
        for (List<String> t : ltimes) {
            DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(bucketName);
            for (String s : t) {
                deleteRequest.addKeyAndVersion(s);
            }
            //批量删除
            DeleteObjectsResult deleteResult = obsClient.deleteObjects(deleteRequest);
            // 获取删除成功的对象
//        System.out.println(deleteResult.getDeletedObjectResults());
            log.info("获取删除成功的对象：" + Arrays.toString((deleteResult.getDeletedObjectResults()).toArray()));
            // 获取删除失败的对象
            log.info("删除失败的对象有：" + Arrays.toString((deleteResult.getErrorResults()).toArray()));
        }


//删除单个对象
//        for (String f : fileNames) {
//            obsClient.deleteObject(bucketName, f);
//        }
    }


}
