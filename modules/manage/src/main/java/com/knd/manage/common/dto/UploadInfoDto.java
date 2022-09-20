package com.knd.manage.common.dto;

import lombok.Data;

@Data
public class UploadInfoDto {
    //数字签名
    private String eSignature;
    //终端
    private String endPoint;
    //桶名
    private String bucketname;
    //访问密钥ID
    private String accessKeyId;
    //安全策略描述
    private String policy;
    //内容类型
    private String contentType;
    //对象名称
    private String key;

}
