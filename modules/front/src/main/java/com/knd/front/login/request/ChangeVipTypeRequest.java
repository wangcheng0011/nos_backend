package com.knd.front.login.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author will
 */
@Data
public class ChangeVipTypeRequest {
    @ApiModelProperty(value = "用户id")
    private String userId;
//    @ApiModelProperty(value = "会员手机号")
//    @NotBlank(message = "会员手机号不能为空")
//    @Pattern(
//            regexp = "1(([38]\\d)|(5[^4&&\\d])|(4[579])|(7[0135678]))\\d{8}",
//            message = "手机号格式不合法"
//    )
//    private String mobile;
    @ApiModelProperty(value = "套餐id")
    @NotBlank(message = "套餐不能为空")
    private String vipMenuId;
}