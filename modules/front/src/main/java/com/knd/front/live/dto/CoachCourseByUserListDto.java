package com.knd.front.live.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 教练课程预约列表dto
 */
@Data
public class CoachCourseByUserListDto {

    @ApiModelProperty(value = "教练课程id")
    private String courseId;

    @ApiModelProperty(value = "教练课程时间id")
    private String timeId;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "课程类别:0课前咨询 1私教课程 2团课")
    private String courseType;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "直播状态 0未开播，1正在直播，2直播结束")
    private String liveStatus;

    @ApiModelProperty(value = "录像地址")
    private String replayUrl;

    @ApiModelProperty(value = "封面图url")
    private String picAttachUrl;

    @ApiModelProperty(value = "难度")
    private String difficulty;

    @ApiModelProperty(value = "训练目标")
    private List<String> targetList;

    @ApiModelProperty(value = "训练部位")
    private List<String> partList;


    @ApiModelProperty(value = "教练用户id")
    private String coachUserId;

    @ApiModelProperty(value = "教练姓名")
    private String coachName;

    @ApiModelProperty(value = "教练头像url")
    private String coachHeadUrl;

    @ApiModelProperty(value = "预约时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime orderTime;

}
