package com.knd.front.train.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@Data
public class ActionArrayTrainListDto {
    @ApiModelProperty(value = "训练类型1:课程|2:动作|3:动作序列")
    private String trainType;
    @ApiModelProperty(value = "记录id")
    private String trainReportId;
    @ApiModelProperty(value = "训练名称")
    private String trainName;
    @ApiModelProperty(value = "训练时间")
    private String trainTime;
    @ApiModelProperty(value = "训练时长")
    private String actualTrainSeconds;
    @ApiModelProperty(value = "训练总力")
    private String finishTotalPower;
}