package com.knd.manage.user.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/6/30
 * @Version 1.0
 */
@Data
public class RegisterRequest {
    @NotBlank(message = "昵称不能为空")
    private String nickName;

    @Pattern(
            regexp = "1(([38]\\d)|(5[^4&&\\d])|(4[579])|(7[0135678]))\\d{8}",
            message = "手机号格式不合法"
    )
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    //@NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?![0-9]*$)(?![a-zA-Z]*$)[0-9A-Za-z-_\\~!@#\\$%\\^&\\*\\.\\(\\)\\[\\]\\{\\}<>\\?\\\\\\/\\'\\\"\\+;:,|=]{6,30}$", message = "密码格式为6位数以上，数字加英文或符号组合")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String code;

    @NotBlank(message = "codeid不能为空")
    private String verifyCodeId;

    @Valid
    private UserDetailRequest userDetailRequest;

    private String sid;

    private String platform;

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

    @ApiModelProperty(value = "苹果Id")
    private String appleId;

    @ApiModelProperty(value = "苹果昵称")
    private String appleNickname;

    @ApiModelProperty(value = "登录state")
    private String loginState;



}