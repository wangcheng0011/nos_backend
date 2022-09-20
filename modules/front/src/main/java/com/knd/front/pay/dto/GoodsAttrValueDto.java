package com.knd.front.pay.dto;

import lombok.Data;


/**
 * @author will
 */
@Data
public class GoodsAttrValueDto {

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
