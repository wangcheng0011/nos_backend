package com.knd.front.user.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author will
 */
@Data
public class ObligationRequest {

    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @Pattern(regexp = "^(1|2|3|5)$",message = "订单类型只能是1vip购买2商品购买3课程购买5直播预约")
    @NotBlank(message = "订单类型不能为空")
    @ApiModelProperty(value = "订单类型")
    private String orderType;

    @ApiModelProperty(value = "收件地址id")
    private String userReceiveAddressId;

    @ApiModelProperty(value = "是否需要配送安装:0-否1-是")
    private String installFlag;

    @ApiModelProperty(value = "安装状态[0->待分配；1->待接收;2->待安装；3->安装中；4->已确认安装]")
    private String installStatus;

    @ApiModelProperty(value = "订单Id")
    private String orderId;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

  /*  @ApiModelProperty(value = "订单来源:1-线上2-线下3-网站")
    private String orderSource;*/



}