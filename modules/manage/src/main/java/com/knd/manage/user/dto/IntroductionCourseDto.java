package com.knd.manage.user.dto;

import lombok.Data;


@Data
public class IntroductionCourseDto {
    //课程id
    private String id;
    //昵称
    private String nickName;
    //手机号
    private String mobile;
    //视频播放开始时间
    private String vedioBeginTime;
    //课程类型,多个则以逗号分隔
    private String type;
    //设备编号
    private String equipmentNo;
    //课程名称
    private String course;

}
