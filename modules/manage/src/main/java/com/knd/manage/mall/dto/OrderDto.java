package com.knd.manage.mall.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.manage.mall.entity.OrderIcEntity;
import com.knd.manage.mall.entity.UserReceiveAddressEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @author will
 */
@Data
@ApiModel(value="OrderDto", description="")
public class OrderDto {

    @ApiModelProperty(value = "主键ID")
    protected String id;

    @ApiModelProperty(value = "会员ID")
    private String userId;


    @ApiModelProperty(value = "总金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "支付方式：1支付宝,2微信")
    private String paymentType;


    @ApiModelProperty(value = "订单类型")
    private String orderType;



    @ApiModelProperty(value = "外部订单号")
    private String outOrderNo;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "状态：1未付款,2已付款,3已关闭,4已完成，5已退款")
    private String status;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime orderCreateTime;


    @ApiModelProperty(value = "购买数量")
    private String quantity;

    @ApiModelProperty(value = "商品")
    private GoodsDto goodsDto;

    @ApiModelProperty(value = "订单明细")
    private List<OrderItemDto> orderItemDtoList;

    @ApiModelProperty(value = "作成者")
    protected String createBy;

    @ApiModelProperty(value = "下单时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime createDate;

    @ApiModelProperty(value = "预计送达时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    protected LocalDate deliveryDate;

    @ApiModelProperty(value = "备注")
    private String remarks;

//    @ApiModelProperty(value = "商品属性")
//    private List<GoodsAttrValueEntity> goodsAttrValueEntityList;

    @ApiModelProperty(value = "收件地址")
    private UserReceiveAddressEntity userReceiveAddressEntity;

    @ApiModelProperty(value = "是否需要配送安装:0-否1-是")
    private String installFlag;
    @ApiModelProperty(value = "线下购买人手机号")
    private String mobile;

    @ApiModelProperty(value = "订单来源:1-线上2-线下3-网站")
    private String orderSource;

    @ApiModelProperty(value = "安装状态[0->待分配；1->待安装；2->安装中；3->已确认安装]")
    private String installStatus;

    @ApiModelProperty(value = "安装单信息")
    private OrderIcEntity orderIcEntity;

    @ApiModelProperty(value = "过期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime orderExpireTime;

    @ApiModelProperty(value = "剩余支付时间")
    private String remainingTime;


    @ApiModelProperty(value = "快递单号")
    private String trackingNumber;

    @ApiModelProperty("物流公司 1德邦 2安能")
    private String logisticsCompanies;

    @ApiModelProperty(value = "序列号")
    private String serialNo;




}
