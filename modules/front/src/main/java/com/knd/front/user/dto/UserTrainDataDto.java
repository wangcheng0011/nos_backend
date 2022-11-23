package com.knd.front.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserTrainDataDto {
    @ApiModelProperty(value = "最大爆发力")
    private String maxExplosiveness = "0";
    @ApiModelProperty(value = "平均爆发力")
    private String avgExplosiveness  = "0";
    @ApiModelProperty(value = "卡路里")
    private String totalCalorie  = "0";
    @ApiModelProperty(value = "总训练时间")
    private String totalTrainSeconds  = "0";
    @ApiModelProperty(value = "总训练课程数")
    private String finishCourseCount  = "0";
    @ApiModelProperty(value = "完成总次数")
    private String finishCounts  = "0";
    @ApiModelProperty(value = "历史最高连续训练天数")
    private String maxConsecutiveDays  = "0";
    @ApiModelProperty(value = "最近连续训练天数")
    private String currentConsecutiveDays  = "0";
}