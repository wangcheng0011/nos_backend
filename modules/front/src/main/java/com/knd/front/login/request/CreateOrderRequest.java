package com.knd.front.login.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
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
public class CreateOrderRequest {
    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;
    @Pattern(regexp = "^(1|2|3|)$",message = "订单类型只能是1vip购买2商品购买3课程购买")
    @NotBlank(message = "订单类型不能为空")
    @ApiModelProperty(value = "订单类型")
    private String orderType;
    @ApiModelProperty(value = "金额")
    @NotNull(message = "金额不能为空")
    @DecimalMin("0.01")
    private BigDecimal amount;

    @ApiModelProperty(value = "支付类型1支付宝或2微信")
    private String paymentType;
    @ApiModelProperty(value = "主题描述")
    private String description;
//    @ApiModelProperty(value = "异步回调url")
//    private String notifyUrl;


//    @Pattern(regexp = "^(1|2|)$",message = "订单类型只能是1会员购买或2商品购买")
//    @NotBlank(message = "订单类型不能为空")
//    @ApiModelProperty(value = "商品类型：1会员购买,2商品购买")
//    private String goodsType;
//
//    @ApiModelProperty(value = "购买商品Id")
//    private String goodsId;
//
//    @ApiModelProperty(value = "购买数量")
//    private String quantity;

    @Valid
    @ApiModelProperty(value = "购买商品列表")
    List<GoodsRequest> goodsRequestList;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "收件地址id")
    private String userReceiveAddressId;

    @ApiModelProperty(value = "订单Id")
    private String id;

    @ApiModelProperty(value = "微信code")
    private String code;

    @ApiModelProperty(value = "openId")
    private String openId;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "平台")
    private String platform;







}