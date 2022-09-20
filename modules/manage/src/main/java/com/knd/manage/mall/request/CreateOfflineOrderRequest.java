package com.knd.manage.mall.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author will
 */
@Data
@ApiModel("创建线下订单请求对象")
public class CreateOfflineOrderRequest {

    @ApiModelProperty(value = "订单金额")
    @NotNull
    @DecimalMin(value = "0",message = "订单金额不能小于0")
    private BigDecimal amount;

    @ApiModelProperty(value = "订单来源")
//    @NotBlank(message = "订单来源不能为空")
    @Pattern(regexp = "^(2)$",message = "订单来源只能是2")
    private String orderSource ="2";

    @Pattern(regexp = "^(2|4)$",message = "订单类型只能是2-配件购买4-机器购买")
    @NotBlank(message = "订单类型不能为空")
    @ApiModelProperty(value = "订单类型")
    private String orderType;

    @ApiModelProperty(value = "支付方式：1支付宝,2微信3.银行卡4.现金（线下）")
    @NotBlank(message = "支付方式不能为空")
    @Pattern(regexp = "^(1|2|3|4)$",message = "支付方式只能是1支付宝,2微信3.银行卡4.现金（线下）")
    private String paymentType;

    @ApiModelProperty(value = "主题描述")
    private String description;


//    @ApiModelProperty(value = "创建订单时间")
//    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private LocalDateTime createDate;
//
//    @ApiModelProperty(value = "订单支付时间")
//    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private LocalDateTime paymentTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "收货地址对象")
    private UserReceiveAddressRequest userReceiveAddressRequest;

    @ApiModelProperty(value = "订单子项")
    List<TbOrderItemRequest> tbOrderItemRequestList;

    @Pattern(regexp = "^(0|1)$",message = "安装标识只能为0-无需安装 1-需要安装")
    @NotBlank(message = "安装标识不能为空")
    @ApiModelProperty(value = "安装标识")
    private String installFlag;

    @ApiModelProperty(value = "安装信息")
    private OrderIcRequest orderIcRequest;

    @ApiModelProperty(value = "userId")
    private String userId;


}