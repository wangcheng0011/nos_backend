package com.knd.manage.mall.dto;

import com.knd.manage.mall.entity.GoodsAttrValueEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/17
 * @Version 1.0
 */
@Data
public class OrderItemDto {


    private String goodsName;

    /*
     * 商品介绍
     */
    private String goodsDesc;

    /**
     * 商品图片附件id
     */
    private String coverUrl;

    /**
     * 价格
     */
    private BigDecimal price;

    @ApiModelProperty(value = "购买数量")
    private String quantity;

    @ApiModelProperty(value = "主题描述")
    private String description;


    @ApiModelProperty(value = "商品属性")
    private List<GoodsAttrValueEntity> goodsAttrValueEntityList;




}