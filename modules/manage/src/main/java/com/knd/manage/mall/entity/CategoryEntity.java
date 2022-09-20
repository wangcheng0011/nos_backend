package com.knd.manage.mall.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import lombok.Data;


/**
 * @author will
 */
@Data
@TableName("pms_category")
public class CategoryEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 分类名称
	 */
	private String categoryName;

	/**
	 * 排序
	 */
	private String sort;


}
