package com.knd.manage.mall.dto;

import com.knd.manage.mall.entity.GoodsAttrValueEntity;
import com.knd.manage.mall.entity.GoodsImgEntity;
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
     * 是否需要配送安装:0-否1-是
     */
    private String installFlag;


    /**
     * 分类id
     */
    private String categoryId;



    /**
     * 价格
     */
    private BigDecimal price;


    private ImgDto coverImg;

    List<GoodsAttrValueEntity> attrList;

    List<ImgDto> headImgList;

    List<ImgDto> infoImgList;


}
