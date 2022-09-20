package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import lombok.Data;


/**
 * @author will
 */
@Data
@TableName("pms_goods_attr_value")
public class GoodsAttrValueEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品Id
	 */
	private String goodsId;
	/**
	 * 属性Id
	 */
	private String attrId;

	/**
	 * 属性名称
	 */
	private String attrName;

	/**
	 * 属性值
	 */
	private String attrValue;
	/**
	 * 顺序
	 */
	private String sort;

}
