package com.knd.front.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserCoursePayDto {

    private String id;

    @ApiModelProperty(value = "课程名称")
    private String course;

    @ApiModelProperty(value = "封面图片")
    private String picAttachUrl;

    @ApiModelProperty(value = "课程类型")
    private String courseType;

    @ApiModelProperty(value = "训练次数")
    private Integer trainNum;

    @ApiModelProperty(value = "价格")
    private BigDecimal amount;

    @ApiModelProperty(value = "是否购买")
    private boolean isPay;

    @ApiModelProperty(value = "视频介绍描述")
    private String remark;

    @ApiModelProperty(value = "视频总时长-分钟")
    private String videoDurationMinutes;

    @ApiModelProperty(value = "难度")
    private String difficulty;

    @ApiModelProperty(value = "训练课程标识")
    private String trainingFlag;


//    @ApiModelProperty(value = "适用人群介绍")
//    private String fitCrowdRemark;
//
//    @ApiModelProperty(value = "禁忌人群介绍")
//    private String unFitCrowdRemark;
//
//    @ApiModelProperty(value = "是否大屏显示")
//    private String appHomeFlag;
//
//    @ApiModelProperty(value = "显示顺序")
//    private String sort;
//
//    @ApiModelProperty(value = "适用会员范围")
//    private String userScope;
//
//    @ApiModelProperty(value = "介绍视频")
//    private String videoAttachUrl;
//
//    @ApiModelProperty(value = "视频总时长-秒")
//    private String videoDurationSeconds;

}
