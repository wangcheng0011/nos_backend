package com.knd.pay.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.mybatis.BaseEntity;
import com.knd.pay.entity.TbOrderItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author will
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TbOrderDto extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "会员ID")
    private String userId;

    @ApiModelProperty(value = "总金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "支付方式：1支付宝,2微信,3银联")
    private String paymentType;

    @ApiModelProperty(value = "主题描述")
    private String description;


    @ApiModelProperty(value = "外部订单号")
    private String outOrderNo;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "状态：1未付款,2已付款,3已关闭,4已退款")
    private String status;

    @ApiModelProperty(value = "剩余支付时间")
    @TableField(exist = false)
    private String remainingTime;


    @ApiModelProperty(value = "ali支付二维码")
    private String aliQrCode;

    @ApiModelProperty(value = "wx支付二维码")
    private String wxQrCode;

    @ApiModelProperty(value = "订单类型：1会员购买,2商品购买,3课程购买4直播预约")
    private String orderType;

    //    @ApiModelProperty(value = "商品类型：1会员购买,2商品购买")
//    private String goodsType;
//
    @ApiModelProperty(value = "购买商品Id")
    private String goodsId;
//
//    @ApiModelProperty(value = "购买数量")
//    private String quantity;

    @ApiModelProperty(value = "收件地址id")
    private String userReceiveAddressId;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "支付时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime paymentTime;


    @ApiModelProperty(value = "发货时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime deliveryTime;


    @ApiModelProperty(value = "确认收货时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime receiveTime;

    @ApiModelProperty(value = "确认收货状态[0->未确认；1->已确认]")
    private String confirmStatus;

    @ApiModelProperty(value = "确认收货码")
    private String receivingCode;

    @ApiModelProperty(value = "退款时间")
    private LocalDateTime refundTime;

    @ApiModelProperty(value = "外部退款单号")
    private String outRefundNo;

    @ApiModelProperty(value = "退款单号")
    private String refundNo;


    @ApiModelProperty(value = "订单取消时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime cancelTime;


    @ApiModelProperty(value = "预期送达时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDate deliveryDate;

    @TableField(exist=false)
    @ApiModelProperty(value = "订单明细")
    private List<TbOrderItem> tbOrderItemList;

    @ApiModelProperty(value = "app支付预订单信息")
    private String appPayInfo;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime orderCreateTime;

    @ApiModelProperty(value = "过期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime orderExpireTime;

    @ApiModelProperty(value = "webUrl")
    private String webUrl;

    @ApiModelProperty(value = "codeUrl")
    private String codeUrl;

    @ApiModelProperty(value = "openId")
    private String openId;

    @ApiModelProperty(value = "aliPay支付链接")
    private String aliPayUrl;







}
