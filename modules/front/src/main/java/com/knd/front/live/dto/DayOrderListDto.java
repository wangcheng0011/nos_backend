package com.knd.front.live.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.front.dto.OrderUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 教练课程预约列表dto
 * @author zm
 */
@Data
public class DayOrderListDto {

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

    @ApiModelProperty(value = "课程单价")
    private BigDecimal coursePrice;

    @ApiModelProperty(value = "预约人数")
    private int orderNum;

    @ApiModelProperty(value = "直播状态")
    private String liveStatus;

    @ApiModelProperty(value = "教练用户id")
    private String coachUserId;

    @ApiModelProperty(value = "教练课程id")
    private String courseId;

    @ApiModelProperty(value = "预约用户信息")
    private List<OrderUser> orderUsers;



}
