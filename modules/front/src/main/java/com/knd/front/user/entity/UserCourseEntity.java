package com.knd.front.user.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserCourseEntity {

    private String id;
    private String course;
    private String remark;
    private String picAttachUrl;
    private String videoDurationMinutes;
    @ApiModelProperty(value = "价格")
    private BigDecimal amount;

    @ApiModelProperty(value = "是否大屏显示")
    private BigDecimal appHomeFlag;

    @ApiModelProperty(value = "发布状态")
    private BigDecimal releaseFlag;

    @ApiModelProperty(value = "课程类型")
    private String courseType;

    @ApiModelProperty(value = "难度")
    private String difficulty;

    @ApiModelProperty(value = "训练课程标识")
    private String trainingFlag;

    @ApiModelProperty(value = "创建时间")
    private String createDate;

    @ApiModelProperty(value = "目标Id")
    private String targetId;

    @ApiModelProperty(value = "健身目标")
    private String target;

    @ApiModelProperty(value = "部位Id")
    private String partId;

    @ApiModelProperty(value = "部位")
    private String part;

}
