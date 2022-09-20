package com.knd.manage.homePage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zm
 */
@Data
public class DiffUserDeviceCoursePlanNumDto {

    @ApiModelProperty(value = "用户数量")
    private String userTotal;

    @ApiModelProperty(value = "用户数量月增长")
    private String userTotalGrowth;

    @ApiModelProperty(value = "设备数量")
    private String deviceTotal;

    @ApiModelProperty(value = "设备数量月增长")
    private String deviceTotalGrowth;

    @ApiModelProperty(value = "课程数量")
    private String courseTotal;

    @ApiModelProperty(value = "课程数量月增长")
    private String courseTotalGrowth;

    @ApiModelProperty(value = "计划数量")
    private String planTotal;

    @ApiModelProperty(value = "计划数量月增长")
    private String planTotalGrowth;

}
