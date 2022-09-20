package com.knd.manage.equip.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ApkInfoDto {
    //apk附件Url
    private String attachUrl;
    //版本信息id
    private String id;
    //版本号
    private String appVersion;
    //版本号前缀
    private String versionPrefix;
    //apk附件id
    private String attachId;
    //发布状态，0：不发布，1：发布
    private String releaseStatus;
    //是否强制更新，0：否，1：是
    private String forceFlag;

    //app类型，0：APP，1：固件
    private String appType;
    //发布内容
    private String content;
    //发布时间
    private String releaseTime;

    //
    //
    //apk附件原名称
    private String attachName;
    //apk附件新名称
    private String attachNewName;
    //apk附件大小
    private String attachSize;

}
