package com.knd.front.entity;

import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "User_Pay对象", description = "")
public class UserPay extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "关联主键id")
    private String payId;

    @ApiModelProperty(value = "购买人员Id")
    private String userId;

    @ApiModelProperty(value = "购买状态 0购买中 1已购买 2退款")
    private String payStatus;
}
