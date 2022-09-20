package com.knd.front.live.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 已预约教练课程列表dto
 * @author zm
 */
@Data
public class CoachOrderListDto {

    @ApiModelProperty(value = "教练姓名")
    private String coachName;

    @ApiModelProperty(value = "教练头像url")
    private String coachHeadUrl;

    @ApiModelProperty(value = "教练课程时间id")
    private String timeId;

    @ApiModelProperty(value = "预约类别0课前咨询 1私教课程 2团课直播 3小组 4训练计划")
    private String orderType;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

}
