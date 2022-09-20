package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


/**
 * @author will
 */
@Data
@EqualsAndHashCode
@TableName("tb_order_item")
@ApiModel(value="TbOrderItem", description="")
public class TbOrderItem {

    private static final long serialVersionUID=1L;

    @TableId
    @ApiModelProperty(value = "主键ID")
    protected String id;

    @ApiModelProperty(value = "订单主表Id")
    private String tbOrderId;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "购买数量")
    private String quantity;

    @ApiModelProperty(value = "购买商品Id")
    private String goodsId;

}
