package com.knd.manage.user.request;

import com.knd.common.em.LoginTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/1
 * @Version 1.0
 */
@Data
public class LoginRequest {

    @ApiModelProperty(value = "登陆方式")
    private LoginTypeEnum loginTypeEnum;

    //apple或者手机号登录token
    private String token;
    //苹果id
    private String appleId;

    @Pattern(
            regexp = "1(([38]\\d)|(5[^4&&\\d])|(4[579])|(7[0135678]))\\d{8}",
            message = "手机号格式不合法"
    )
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @Pattern(regexp = "^(?![0-9]*$)(?![a-zA-Z]*$)[0-9A-Za-z-_\\~!@#\\$%\\^&\\*\\.\\(\\)\\[\\]\\{\\}<>\\?\\\\\\/\\'\\\"\\+;:,|=]{6,30}$", message = "密码格式为6位数以上，数字加英文或符号组合")
    private String password;

    @ApiModelProperty(value = "手机验证码")
    private String verifyCode;

    @ApiModelProperty(value = "手机验证码Id")
    private String verifyCodeId;

    @ApiModelProperty(value = "微信/QQ 登录参数1")
    private String openId;

    @ApiModelProperty(value = "unionId")
    private String unionId;

    @ApiModelProperty(value = "qqOpenId")
    private String qqOpenId;

    @ApiModelProperty(value = "微信/QQ 昵称")
    private String wxNickname;

    @ApiModelProperty(value = "微信/QQ 头像")
    private String wxAvatar;

    @ApiModelProperty(value = "登录设备编号")
    private String equipmentNo;

    @ApiModelProperty(value = "机器编号")
    private String sid;

    @ApiModelProperty(value = "登陆平台 ios/android/quinnoid")
    private String platform;

    @ApiModelProperty(value = "openIdcode")
    private String openIdcode;

    @ApiModelProperty(value = "encryptedData")
    private String encryptedData;

    @ApiModelProperty(value = "iv")
    private String  iv;

    @ApiModelProperty(value = "手机code")
    private String mobileCode;

    @ApiModelProperty(value = "微信/QQ 昵称")
    private String nickname;

    @ApiModelProperty(value = "微信/QQ 头像")
    private String avatar;

    @ApiModelProperty(value = "登录state")
    private String loginState;

    @ApiModelProperty(value = "小程序头像")
    private String smallRoutineHeadPic;






}