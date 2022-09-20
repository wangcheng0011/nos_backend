package com.knd.front.pay.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author will
 */
@Data
public class TbOrderItemRequest {

    @ApiModelProperty(value = "购买数量")
    private String quantity;

    @ApiModelProperty(value = "购买商品Id")
    private String goodsId;

//    @ApiModelProperty(value = "商品对象")
//    private CreateGoodsRequest createGoodsRequest;

}
