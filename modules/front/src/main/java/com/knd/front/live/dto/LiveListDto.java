package com.knd.front.live.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author zm
 */
@Data
@ApiModel(description = "直播列表")
public class LiveListDto {

    @ApiModelProperty(value = "课程时间id")
    private String timeId;

    @ApiModelProperty(value = "日期")
    private String date;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "课程时间（分）")
    private String courseTime;

    @ApiModelProperty(value = "能量消耗（千卡）")
    private String consume;

    @ApiModelProperty(value = "录像地址")
    private String replayUrl;

    @ApiModelProperty(value = "封面图url")
    private String picAttachUrl;

    @ApiModelProperty(value = "难度")
    private String difficulty;

    @ApiModelProperty(value = "是否已预约0否 1是")
    private String isOrder;

    @ApiModelProperty(value = "教练头像url")
    private String coachHeadUrl;

    @ApiModelProperty(value = "教练姓名")
    private String coachName;

    @ApiModelProperty(value = "教练用户id")
    private String coachUserId;

    @ApiModelProperty(value = "预约人数")
    private int orderNum;

    @ApiModelProperty(value = "课程类型：0课前咨询 1私教课程 2团课")
    private String courseType;

    @ApiModelProperty(value = "直播状态0未开播，1正在直播，2直播结束")
    private String liveStatus;

}
