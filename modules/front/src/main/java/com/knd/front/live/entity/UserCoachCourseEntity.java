package com.knd.front.live.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 教练课程表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lb_user_coach_course")
@ApiModel(value="UserCoachCourseEntity对象", description="")
public class UserCoachCourseEntity extends BaseEntity {

    @ApiModelProperty(value = "教练id")
    private String coachId;

    @ApiModelProperty(value = "课程类别0课前咨询 1私教课程 2团课")
    private String courseType;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "课程简介")
    private String courseSynopsis;

    @ApiModelProperty(value = "难度id")
    private String difficultyId;

    @ApiModelProperty(value = "课程时间（分）")
    private String courseTime;

    @ApiModelProperty(value = "能量消耗（千卡）")
    private String consume;

    @ApiModelProperty(value = "封面图片id")
    private String picAttachId;

    @ApiModelProperty(value = "训练目的,支持多选：使用,隔开")
    private String targets;

    @ApiModelProperty(value = "训练部位,支持多选：使用,隔开")
    private String parts;

}
