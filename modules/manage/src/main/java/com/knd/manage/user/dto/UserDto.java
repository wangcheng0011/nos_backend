package com.knd.manage.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    @ApiModelProperty(value = "会员id")
    protected String userId;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "注册时间")
    private String registTime;

    @ApiModelProperty(value = "出生时间")
    private String birthDay;

    @ApiModelProperty(value = "身高")
    private String height;

    @ApiModelProperty(value = "体重")
    private String weight;

    @ApiModelProperty(value = "性别 0男1女")
    private String gender;

    @ApiModelProperty(value = "当前训练周期等级")
    private String trainLevel;

    @ApiModelProperty(value = "当前训练周期开始日期")
    private String trainPeriodBeginTime;

    @ApiModelProperty(value = "冻结标识")
    private String frozenFlag;

    @ApiModelProperty(value = "会员类型 0普通会员 1个人会员 2家庭会员-主 3家庭会员-副")
    private String vipStatus;

    @ApiModelProperty(value = "会员开始时间")
    private LocalDate vipBeginDate;

    @ApiModelProperty(value = "会员结束时间")
    private LocalDate vipEndDate;

    @ApiModelProperty(value = "器械Id")
    private String sid;

    @ApiModelProperty(value = "苹果id")
    private String appleId;

    @ApiModelProperty(value = "苹果昵称")
    private String appleNickname;

    @ApiModelProperty(value = "qq登陆id")
    private String qqOpenId;

    @ApiModelProperty(value = "qq昵称")
    private String qqNickname;

    @ApiModelProperty(value = "qq头像url")
    private String qqAvatar;

    @ApiModelProperty(value = "微信登陆id")
    private String wxOpenId;

    @ApiModelProperty(value = "unionId")
    private String unionId;

    @ApiModelProperty(value = "微信昵称")
    private String wxNickname;

    @ApiModelProperty(value = "微信头像url")
    private String wxAvatar;

    @ApiModelProperty(value = "推荐部位")
    private String recommendBodyPart;

    @ApiModelProperty(value = "access_token")
    private String access_token;



}

