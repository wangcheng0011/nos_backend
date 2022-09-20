package com.knd.front.live.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author zm
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@Data
public class OrderCoachRequest {

    @ApiModelProperty(value = "课程时间id")
    private String coachTimeId;

    @ApiModelProperty(value = "用户id")
    @Size(max = 64)
    @NotBlank
    private String userId;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "支付类型1支付宝或2微信")
    private String paymentType;

}