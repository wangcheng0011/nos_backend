package com.knd.front.pay.dto;

import com.knd.front.entity.GoodsAttrValueEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


/**
 * @author will
 */
@Data
@ApiModel(value="OrderDto", description="")
public class OrderItemDto {
    private String id;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品介绍
     */
    private String goodsDesc;
    /**
     * 封面图片附件id
     */
    private String coverUrl;

    /**
     * 价格
     */
    private BigDecimal price;

    @ApiModelProperty(value = "购买数量")
    private String quantity;

    private List<GoodsAttrValueEntity> goodsAttrValueEntityList;
}
