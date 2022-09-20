package com.knd.front.pay.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.front.entity.OrderIcEntity;
import com.knd.front.entity.TbOrderItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author will
 */
@Data
@ApiModel("创建订单请求对象")
public class CreateOrderRequest {

    @ApiModelProperty(value = "会员ID")
    @NotBlank(message = "会员ID不能为空")
    private String userId;

    @ApiModelProperty(value = "订单金额")
    @NotNull
    @DecimalMin(value = "0",message = "订单金额不能小于0")
    private BigDecimal amount;

    @ApiModelProperty(value = "订单来源")
//    @NotBlank(message = "订单来源不能为空")
//    @Pattern(regexp = "^(1|2|3|)$",message = "订单来源只能是1-线上2-线下3-网站")
    private String orderSource ="3";

    @Pattern(regexp = "^(1|2|3|4)$",message = "订单类型只能是1-vip购买2-商品购买3-课程购买4-机器购买")
    @NotBlank(message = "订单类型不能为空")
    @ApiModelProperty(value = "订单类型")
    private String orderType;

    @ApiModelProperty(value = "支付类型1支付宝或2微信")
    @NotBlank(message = "支付方式不能为空")
    @Pattern(regexp = "^(1|2|3|4)$",message = "支付方式只能是1支付宝,2微信,3.银联4.现金（线下）")
    private String paymentType;

    @ApiModelProperty(value = "主题描述")
    private String description;


    @ApiModelProperty(value = "平台订单号（支付宝微信）")
    @NotBlank(message = "平台订单号（支付宝微信）不能为空")
    private String outOrderNo;

    @ApiModelProperty(value = "业务订单号")
    @NotBlank(message = "业务订单号不能为空")
    private String orderNo;

    @ApiModelProperty(value = "创建订单时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "订单支付时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime paymentTime;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "收货地址id")
    @NotBlank(message = "收货地址id不能为空")
    private String userReceiveAddressId;

    @ApiModelProperty(value = "订单子项")
    List<TbOrderItem> tbOrderItemList;

    @Pattern(regexp = "^(0|1)$",message = "安装标识只能为0-无需安装 1-需要安装")
    @NotBlank(message = "安装标识不能为空")
    @ApiModelProperty(value = "安装标识")
    private String installFlag;

    @ApiModelProperty(value = "安装信息子项")
    private OrderIcEntity orderIcEntity;


}