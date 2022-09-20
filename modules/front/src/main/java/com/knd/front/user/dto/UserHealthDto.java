package com.knd.front.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserHealthDto {

    @ApiModelProperty(value = "目标体重",required = true)
    private String targetWeight;

    @ApiModelProperty(value = "目标身高",required = true)
    private String targetHeight;

    @ApiModelProperty(value = "目标胸围",required = true)
    private String targetBust;

    @ApiModelProperty(value = "目标腰围",required = true)
    private String targetWaist;

    @ApiModelProperty(value = "目标臀围",required = true)
    private String targetHipline;

    @ApiModelProperty(value = "目标臂围",required = true)
    private String targetArmCircumference;

    @ApiModelProperty(value = "最新bmi",required = true)
    private String bmi;

    @ApiModelProperty(value = "最新体重",required = true)
    private String currentWeight;

    @ApiModelProperty(value = "最新身高",required = true)
    private String height;

    @ApiModelProperty(value = "最新胸围",required = true)
    private String bust;

    @ApiModelProperty(value = "最新腰围",required = true)
    private String waist;

    @ApiModelProperty(value = "最新臀围",required = true)
    private String hipline;

    @ApiModelProperty(value = "最新臂围",required = true)
    private String armCircumference;
}
