package com.knd.front.login.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/1
 * @Version 1.0
 */
@Data
public class ResetRequest {
    @NotBlank(message = "手机号不能为空")
    @Pattern(
            regexp = "1(([38]\\d)|(5[^4&&\\d])|(4[579])|(7[0135678]))\\d{8}",
            message = "手机号格式不合法"
    )
    private String mobile;
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^(?![0-9]*$)(?![a-zA-Z]*$)[0-9A-Za-z-_\\~!@#\\$%\\^&\\*\\.\\(\\)\\[\\]\\{\\}<>\\?\\\\\\/\\'\\\"\\+;:,|=]{6,30}$", message = "密码格式为6位数以上，数字加英文或符号组合")
    private String password;
    @NotBlank(message = "验证码不能为空")
    private String code;
    @NotBlank(message = "codeid不能为空")
    private String verifyCodeId;
}