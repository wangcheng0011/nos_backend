package com.knd.front.train.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/7
 * @Version 1.0
 */
@Data
public class FreeTrainRequest {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id")
    private String userId;
    @NotBlank(message = "运动总时间(s)不能为空")
    @ApiModelProperty(value = "运动总时间(s)")
    private String totalSeconds;
    @NotBlank(message = "实际运动总时间(s)不能为空")
    @ApiModelProperty(value = "实际运动总时间(s)")
    private String actTrainSeconds;
    @NotBlank(message = "完成总次数不能为空")
    @ApiModelProperty(value = "完成总次数")
    private String finishCounts;
    @NotBlank(message = "完成总力不能为空")
    @ApiModelProperty(value = "完成总力")
    private String finishTotalPower;
    @NotBlank(message = "设备编号不能为空")
    @ApiModelProperty(value = "设备编号")
    private String equipmentNo;
    @NotBlank(message = "最大爆发力不能为空")
    @ApiModelProperty(value = "最大爆发力")
    private String maxExplosiveness;
    @NotBlank(message = "平均爆发力不能为空")
    @ApiModelProperty(value = "平均爆发力")
    private String avgExplosiveness;
    @NotBlank(message = "卡路里不能为空")
    @ApiModelProperty(value = "卡路里")
    private String calorie;
    @NotBlank(message = "类型 1自由训练页面 2非运动页面自由训练")
    @ApiModelProperty(value = "类型 1自由训练页面 2非运动页面自由训练")
    private String type;


}