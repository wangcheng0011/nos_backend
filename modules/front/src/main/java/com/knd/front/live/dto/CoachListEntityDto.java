package com.knd.front.live.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 教练列表dto
 */
@Data
public class CoachListEntityDto {

    @ApiModelProperty(value = "教练id")
    private String coachId;

    @ApiModelProperty(value = "教练用户id")
    private String coachUserId;

    @ApiModelProperty(value = "教练姓名")
    private String coachName;

    @ApiModelProperty(value = "学员人数")
    private long traineeNum;

    @ApiModelProperty(value = "私教课程单价")
    private BigDecimal coursePrice;

    @ApiModelProperty(value = "擅长内容")
    private String content;

    @ApiModelProperty(value = "教练描述")
    private String depict;

    @ApiModelProperty(value = "课程数量")
    private String num;
}
