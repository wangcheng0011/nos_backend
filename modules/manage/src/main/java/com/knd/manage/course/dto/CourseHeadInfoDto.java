package com.knd.manage.course.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseHeadInfoDto {
    //课程id
    private String id;
    //课程名称
    private String course;
    //显示顺序
    private String sort;
    //发布状态
    private String releaseFlag;
    //是否在app大屏显示
    private String appHomeFlag;
    //适用会员范围
    private String userScope;
    //发布时间
    private String releaseTime;
    //分类 ,多个以逗号隔开
    private String types;
    //目标 ,多个以逗号隔开
    private String targets;
    //部位 ,多个以逗号隔开
    private String parts;
    //训练课标识，1：是训练课，0：是视频介绍
    private String trainingFlag;

    private String amount;

    private String courseType;
}

