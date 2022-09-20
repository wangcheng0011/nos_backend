package com.knd.manage.basedata.dto;

import lombok.Data;

@Data
public class PlatformInfoDto {

    //注册用户数量
    private String registerUserCount;
    //课程视频数量
    private String courseCount;
    //上报设备数量
    private String reportDeviceCount;
    //设备最新版本
    private String appLatestVersion;


}
