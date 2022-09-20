package com.knd.manage.mall.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author will
 */
@Data
public class GetOrderInfoRequest {
    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "平台")
    private String payPlatform;

    @ApiModelProperty(value = "购买人手机号")
    private String mobile;
    @ApiModelProperty(value = "订单来源")
    @NotBlank(message = "订单来源不能为空")
    @Pattern(regexp = "^(1|2|3|4|5)$",message = "订单来源只能是 1-线上 2-线下 3-网站 4-android 5-ios")
    private String orderSource;
    @ApiModelProperty(value = "支付类型1支付宝或2微信")
    private String paymentType;
    //    @NotBlank(message = "商品id不能为空")
//    @ApiModelProperty(value = "商品id")
//    private String goodsId;
//    @Pattern(regexp = "^(1|2|)$",message = "商品类型只能是1vip购买2商品购买")
//    @NotBlank(message = "商品类型不能为空")
//    @ApiModelProperty(value = "商品类型")
//    private String goodsType;
//
//    @ApiModelProperty(value = "购买数量")
//    private String quantity;
    @Pattern(regexp = "^(0|1|2|3|5)$",message = "订单类型只能是1-vip购买2-商品购买3-课程购买4-机器购买5-直播预约")
    @NotBlank(message = "订单类型不能为空")
    @ApiModelProperty(value = "订单类型：1会员购买,2商品购买,3课程购买")
    private String orderType;



    @ApiModelProperty(value = "订单付款金额（订单来源为网站或者线下方式须传递）")
    private BigDecimal amount;
    @Valid
    @ApiModelProperty(value = "购买商品列表")
    List<GoodsRequest> goodsRequestList;

    @ApiModelProperty(value = "收件地址id")
    private String userReceiveAddressId;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "订单生成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime orderCreateTime;

    @ApiModelProperty(value = "订单过期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime orderExpireTime;

    @ApiModelProperty(value = "订单Id")
    private String id;

    @ApiModelProperty(value = "微信支付code")
    private String code;

    @ApiModelProperty(value = "订单号")
    private String orderNo;





}