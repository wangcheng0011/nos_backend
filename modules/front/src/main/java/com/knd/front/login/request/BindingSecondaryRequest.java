package com.knd.front.login.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author will
 */
@Data
public class BindingSecondaryRequest {
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "家庭主会员手机号")
    @NotBlank(message = "家庭主会员手机号不能为空")
    @Pattern(
            regexp = "1(([38]\\d)|(5[^4&&\\d])|(4[579])|(7[0135678]))\\d{8}",
            message = "家庭主会员手机号格式不合法"
    )
    private String majorMobile;
    @ApiModelProperty(value = "副账户手机号")
    @Pattern(
            regexp = "1(([38]\\d)|(5[^4&&\\d])|(4[579])|(7[0135678]))\\d{8}",
            message = "副账户手机号格式不合法"
    )
    @NotBlank(message = "副账户手机号不能为空")
    private String secondaryMobile;
}