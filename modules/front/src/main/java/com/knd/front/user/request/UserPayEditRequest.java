package com.knd.front.user.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class UserPayEditRequest {

    @NotBlank
    @ApiModelProperty(value = "订单id",required = true)
    private String orderId;

    @NotBlank
    @ApiModelProperty(value = "变更状态 1购买成功 2退款",required = true)
    private String payStatus;
}
