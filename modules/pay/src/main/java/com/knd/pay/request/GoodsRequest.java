package com.knd.pay.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * @author will
 */
@Data
public class GoodsRequest {
    @NotBlank(message = "商品id不能为空")
    @ApiModelProperty(value = "商品id")
    private String goodsId;
//    @Pattern(regexp = "^(1|2|)$",message = "商品类型只能是1vip购买2商品购买")
//    @NotBlank(message = "商品类型不能为空")
//    @ApiModelProperty(value = "商品类型")
//    private String goodsType;
    @ApiModelProperty(value = "购买数量")
    private String quantity;
    @ApiModelProperty(value = "金额")
    private BigDecimal price;


}