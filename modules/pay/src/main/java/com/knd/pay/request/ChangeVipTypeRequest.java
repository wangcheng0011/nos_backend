package com.knd.pay.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author will
 */
@Data
public class ChangeVipTypeRequest {
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "套餐id")
    @NotBlank(message = "套餐不能为空")
    private String vipMenuId;
}