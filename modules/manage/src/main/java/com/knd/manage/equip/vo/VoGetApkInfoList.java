package com.knd.manage.equip.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class VoGetApkInfoList {
    @ApiParam("发布状态，0未发布，1已发布")
    private String releaseStatus;

    @ApiParam("app类型，0 APP， 1 固件")
    private String appType;

    @ApiParam("版本号")
    @Size(max = 64)
    private String appVersion;
    @NotBlank
    @ApiParam("当前页")
    private String current;

}
