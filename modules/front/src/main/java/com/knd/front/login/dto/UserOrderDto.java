package com.knd.front.login.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.front.entity.UserReceiveAddressEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/17
 * @Version 1.0
 */
@Data
public class UserOrderDto {

    private String id;

    private List<UserOrderItemDto> userOrderItemDtoList;

//    private String goodsName;
//    /*
//     * 商品介绍
//     */
//    private String goodsDesc;
//
//    /**
//     * 商品图片附件id
//     */
//    private String coverUrl;
//
//    /**
//     * 价格
//     */
//    private BigDecimal price;

    @ApiModelProperty(value = "总金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "支付方式：1支付宝,2支付宝微信")
    private String paymentType;

    @ApiModelProperty(value = "主题描述")
    private String description;


    @ApiModelProperty(value = "外部订单号")
    private String outOrderNo;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

//    @ApiModelProperty(value = "状态：1未付款,2已付款,3已关闭,4已完成，5已退款")
    /**
     * 1未付款,2待发货,3已发货,4已完成，5已取消,6已退款
     */
    private String status;

    @ApiModelProperty(value = "剩余支付时间")
    @TableField(exist = false)
    private String remainingTime;

    @ApiModelProperty(value = "ali支付二维码")
    private String aliQrCode;

    @ApiModelProperty(value = "wx支付二维码")
    private String wxQrCode;

//    @ApiModelProperty(value = "商品类型：1会员购买,2商品购买")
//    private String goodsType;
//
//    @ApiModelProperty(value = "购买商品Id")
//    private String goodsId;
//
//    @ApiModelProperty(value = "购买数量")
//    private String quantity;

    @ApiModelProperty(value = "收件地址详情")
    private UserReceiveAddressEntity userReceiveAddress;


    @ApiModelProperty(value = "是否需要配送安装:0-否1-是")
    private UserReceiveAddressEntity installFlag;

    @ApiModelProperty(value = "下单人")
    @TableField(fill = FieldFill.INSERT)
    protected String createBy;

    @ApiModelProperty(value = "创建订单时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createDate;

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

    @ApiModelProperty(value = "预期送达时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDate deliveryDate;

    @ApiModelProperty(value = "订单取消时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime cancelTime;

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

    @ApiModelProperty(value = "app支付预订单信息")
    private String appPayInfo;

    @ApiModelProperty(value = "平台")
    private String platform;


    @ApiModelProperty(value = "快递单号")
    private String trackingNumber;

    @ApiModelProperty("物流公司 1德邦 2安能")
    private String logisticsCompanies;


}