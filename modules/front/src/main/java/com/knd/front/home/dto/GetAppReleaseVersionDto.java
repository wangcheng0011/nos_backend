package com.knd.front.home.dto;

import lombok.Data;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/6/30
 * @Version 1.0
 */
@Data
public class GetAppReleaseVersionDto {
    private String appType;
    private String versionPrefix;
    private String appVersion;
    private String apkUrl;
    private String forceFlag;
    private String content;
}