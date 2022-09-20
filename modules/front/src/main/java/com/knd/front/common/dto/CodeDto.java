package com.knd.front.common.dto;

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
public class CodeDto {
    @NotBlank(message = "手机号不能为空")
    @Pattern(
            regexp = "1(([38]\\d)|(5[^4&&\\d])|(4[579])|(7[0135678]))\\d{8}",
            message = "手机号格式不合法"
    )
    private String mobile;
    @NotBlank(message = "验证码类型不能为空")
    private String codeType;

    private String code;
}