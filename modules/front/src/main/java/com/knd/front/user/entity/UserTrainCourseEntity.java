package com.knd.front.user.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserTrainCourseEntity {

    private String id;
    private String course;
    private String remark;
    private String picAttachUrl;
    private String videoDurationMinutes;
    @ApiModelProperty(value = "价格")
    private BigDecimal amount;

    @ApiModelProperty(value = "卡路里")
    private String calorie;

    @ApiModelProperty(value = "课程类型")
    private String courseType;

    @ApiModelProperty(value = "难度")
    private String difficulty;

    @ApiModelProperty(value = "训练时间")
    private LocalDateTime time;
}
