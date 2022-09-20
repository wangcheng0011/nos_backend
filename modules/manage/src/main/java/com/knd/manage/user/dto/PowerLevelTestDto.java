package com.knd.manage.user.dto;

import lombok.Data;

@Data
public class PowerLevelTestDto {
    //id
    private String id;
    //昵称
    private String nickName;
    //手机号
    private String mobile;
    //测试时间
    private String createDate;
    //测试项目
    private String action;
    //最大力
    private String maxPower;
    //测试结果等级
    private String powerLevel;
}
