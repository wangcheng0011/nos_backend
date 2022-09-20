package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import lombok.Data;


/**
 * @author will
 */
@Data
@TableName("pms_goods_img")
public class GoodsImgEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品Id
	 */
	private String goodsId;
	/**
	 * 图片地址
	 */
	private String attachId;
	/**
	 * 图片类型 0-商品介绍图片1-商品详情图片
	 */
	private String imgType;
	/**
	 * 顺序
	 */
	private String sort;

}
