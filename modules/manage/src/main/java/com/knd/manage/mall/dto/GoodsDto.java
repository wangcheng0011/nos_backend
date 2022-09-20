package com.knd.manage.mall.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author will
 */
@Data
@ApiModel(value = "商品对象", description = "")
public class GoodsDto {
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


    private String categoryName;

    /**
     * 发布状态 0-未发布1-已发布
     */
    private String publishStatus;

    /**
     * 是否需要配送安装:0-否1-是
     */
    private String installFlag;


    /**
     * 封面图片附件id
     */
    private String coverUrl;

    /**
     * 价格
     */
    private BigDecimal price;

//    private List<GoodsAttrValueEntity> attrList;
//
//    private List<String> headImgList;
//
//    private List<GoodsInfoImageRequest> infoImgList;


}
