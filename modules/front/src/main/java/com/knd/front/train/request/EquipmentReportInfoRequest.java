package com.knd.front.train.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/8
 * @Version 1.0
 */
@Data
public class EquipmentReportInfoRequest {

    @NotBlank(message = "设备编号不能为空")
    private String equipmentNo;

    @NotBlank(message = "开机时间不能为空")
    private String turnOnTime;

    private String turnOffTime;

    @NotBlank(message = "硬件版本号不能为空")
    private String hardVersion;

    @NotBlank(message = "下控版本号不能为空")
    private String mainboardVersion;

    @NotBlank(message = "app版本号不能为空")
    private String appVersion;

    @NotBlank(message = "位置信息不能为空")
    private String positionInfo;

    @ApiModelProperty(value = "省唯一识别码")
    @NotBlank(message = "省唯一识别码不能为空")
    private String provinceAdcode;

    @ApiModelProperty(value = "市唯一识别码")
    @NotBlank(message = "市唯一识别码不能为空")
    private String cityAdcode;

    @ApiModelProperty(value = "区唯一识别码")
    @NotBlank(message = "区唯一识别码不能为空")
    private String districtAdcode;

}