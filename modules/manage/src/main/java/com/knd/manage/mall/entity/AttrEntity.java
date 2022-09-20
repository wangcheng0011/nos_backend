package com.knd.manage.mall.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import lombok.Data;


/**
 * @author will
 */
@Data
@TableName("pms_attr")
public class AttrEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 属性名
	 */
	private String attrName;



	/**
	 * 所属分类
	 */
	private String categoryId;
	/**
	 * 排序
	 */
	private String sort;

}
