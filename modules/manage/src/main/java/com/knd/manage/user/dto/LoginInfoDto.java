package com.knd.manage.user.dto;

import lombok.Data;

@Data
public class LoginInfoDto {
    //用户id
    private String userId;
    //昵称
    private String nickName;
    //手机号
    private String mobile;
    //登录时间
    private String loginInTime;
    //登录设备编号
    private String equipmentNo;
    //退出时间
    private String loginOutTime;

}
