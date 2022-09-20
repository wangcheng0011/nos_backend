package com.knd.manage.mall.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * @author will
 */
@Data
public class GoodsRequest {
    @NotBlank(message = "商品id不能为空")
    @ApiModelProperty(value = "商品id")
    private String goodsId;
    @ApiModelProperty(value = "购买数量")
    @NotBlank(message = "购买数量不能为空")
    private String quantity;
    @ApiModelProperty(value = "单价")
    private BigDecimal price;


}