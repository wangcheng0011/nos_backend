package com.knd.front.pay.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author will
 */
@Data
public class ParseOrderNotifyRequest {
    @ApiModelProperty(value = "订单号")
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @ApiModelProperty(value = "订单号")
    @NotBlank(message = "订单号不能为空")
    private String resultCode;




}