package com.knd.front.user.dto;

import com.knd.front.home.dto.BaseBodyPartDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@Data
public class TrainListDto {
    @ApiModelProperty(value = "训练类型1:课程|2:动作|3:动作序列|4:自由训练")
    private String trainType;
    @ApiModelProperty(value = "记录id")
    private String trainReportId;
    @ApiModelProperty(value = "训练名称")
    private String trainName;
    @ApiModelProperty(value = "训练时间")
    private String trainTime;
    @ApiModelProperty(value = "课程id")
    private String courseId;
    @ApiModelProperty(value = "训练时长")
    private String actualTrainSeconds;
    @ApiModelProperty(value = "训练总力")
    private String finishTotalPower;
    @ApiModelProperty(value = "运动次数统计")
    private String finishCounts  = "0";
    @ApiModelProperty(value = "卡路里")
    private String calorie;
    @ApiModelProperty(value = "总训练课程数")
    private String finishCourseCount  = "0";

    @ApiModelProperty(value = "难度")
    private String difficulty;

    @ApiModelProperty(value = "封面图片")
    private String picAttachUrl;

    @ApiModelProperty(value = "锻炼部位")
    private List<BaseBodyPartDto> partList;
}