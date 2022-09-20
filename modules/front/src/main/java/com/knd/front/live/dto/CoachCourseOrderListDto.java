package com.knd.front.live.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.common.em.FollowStatusEnum;
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
public class CoachCourseOrderListDto {

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

    @ApiModelProperty(value = "课程类别")
    private String courseType;

    @ApiModelProperty(value = "课程封面图url")
    private String picAttachUrl;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "课程简介")
    private String courseSynopsis;

    @ApiModelProperty(value = "课程单价")
    private BigDecimal coursePrice;

    @ApiModelProperty(value = "课程时间（分）")
    private String courseTime;

    @ApiModelProperty(value = "能量消耗（千卡）")
    private String consume;

    @ApiModelProperty(value = "教练头像url")
    private String coachHeadUrl;

    @ApiModelProperty(value = "教练用户id")
    private String coachUserId;

    @ApiModelProperty(value = "教练姓名")
    private String coachName;

    @ApiModelProperty(value = "直播状态 0未开播，1正在直播，2直播结束")
    private String liveStatus;

    @ApiModelProperty(value = "是否已预约:未预约为空 0预约中 1已预约 2已取消")
    private String isOrder;

    @ApiModelProperty(value = "预约人数")
    private int orderNum;

    @ApiModelProperty(value = "预约人头像,最多五个")
    private List<String> orderHeadPic;

    @ApiModelProperty(value = "关注状态")
    private FollowStatusEnum followStatus;

    @ApiModelProperty(value = "录像地址")
    private String replayUrl;


    @ApiModelProperty(value = "难度")
    private String difficulty;
}
