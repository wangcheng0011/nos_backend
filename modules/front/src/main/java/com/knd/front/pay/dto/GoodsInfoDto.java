package com.knd.front.pay.dto;

import com.knd.front.entity.GoodsAttrValueEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author will
 */
@Data
public class GoodsInfoDto {

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
     * 商品类型 0-虚拟 1-实物
     */
    private String goodsType;


    /**
     * 分类id
     */
    private String categoryId;



    /**
     * 价格
     */
    private BigDecimal price;


    private ImgDto coverImg;

    List<GoodsAttrValueDto> attrList;

    List<ImgDto> headImgList;

    List<ImgDto> infoImgList;


}
