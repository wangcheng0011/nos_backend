package com.knd.pay.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @author will
 */
@Data
@ApiModel(value="ActionArray", description="")
public class PreOrderDto {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "支付剩余时间")
    private String payRemainingTime;

    @ApiModelProperty(value = "阿里支付二维码")
    private String aliQrCode;

    @ApiModelProperty(value = "微信支付二维码")
    private String wxQrCode;


}
