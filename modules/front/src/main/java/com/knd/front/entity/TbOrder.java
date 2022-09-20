package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
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
@TableName("tb_order")
@ApiModel(value="TbOrder", description="")
public class TbOrder extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "会员ID")
    private String userId;

    @ApiModelProperty(value = "总金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "支付类型1支付宝或2微信")
    private String paymentType;

    @ApiModelProperty(value = "主题描述")
    private String description;


    @ApiModelProperty(value = "平台订单号（支付宝微信）")
    private String outOrderNo;

    @ApiModelProperty(value = "业务订单号")
    private String orderNo;

    @ApiModelProperty(value = "状态：1未付款,2已付款,3已发货,4已完成,5已取消,6已退款")
    private String status;

    @ApiModelProperty(value = "剩余支付时间")
    @TableField(exist = false)
    private String remainingTime;

    @ApiModelProperty(value = "ali支付二维码")
    private String aliQrCode;

    @ApiModelProperty(value = "wx支付二维码")
    private String wxQrCode;

    @ApiModelProperty(value = "订单类型：1会员购买,2商品购买,3课程购买")
    private String orderType;

    @ApiModelProperty(value = "订单来源:1-线上2-线下3-网站")
    private String orderSource;

    @ApiModelProperty(value = "收件地址id")
    private String userReceiveAddressId;

    @ApiModelProperty(value = "支付时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime paymentTime;


    @ApiModelProperty(value = "发货时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime deliveryTime;


    @ApiModelProperty(value = "确认收货时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
    protected LocalDate deliveryDate;

    @ApiModelProperty(value = "订单取消时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime cancelTime;

    @TableField(exist=false)
    @ApiModelProperty(value = "订单明细")
    private List<TbOrderItem> tbOrderItemList;

    @ApiModelProperty(value = "下单人手机号")
    private String mobile;

    @ApiModelProperty(value = "配送安装标识 0否 1是")
    private String installFlag;

    @ApiModelProperty(value = "安装状态[0->待分配；1->待接收;2->待安装；3->安装中；4->已确认安装]")
    private String installStatus;

    @ApiModelProperty(value = "app支付预订单信息")
    private String appPayInfo;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "快递单号")
    private String trackingNumber;

    @ApiModelProperty("物流公司 1德邦 2安能")
    private String logisticsCompanies;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime orderCreateTime;

    @ApiModelProperty(value = "过期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime orderExpireTime;


}
