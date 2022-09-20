package com.knd.manage.help.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author wangcheng
 */
@Data
@TableName("manage_help")
public class HelpEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	private String id;

	@ApiModelProperty(value = "标题")
	private String title;

	@ApiModelProperty(value = "标题排序")
	private String titleSort;





}
