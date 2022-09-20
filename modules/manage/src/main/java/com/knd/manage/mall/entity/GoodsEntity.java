package com.knd.manage.mall.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;


/**
 * @author will
 */
@Data
@TableName("pms_goods")
public class GoodsEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

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
	private String coverAttachId;

	/**
	 * 价格
	 */
	private BigDecimal price;

}
