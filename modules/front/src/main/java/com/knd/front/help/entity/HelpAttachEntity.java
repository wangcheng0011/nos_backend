package com.knd.front.help.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author wangcheng
 */
@Data
@TableName("manage_help_attach")
public class HelpAttachEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	private String id;

	@ApiModelProperty(value = "帮助id")
	private String helpId;

	@ApiModelProperty(value = "图片Id")
	private String attachUrlId;


}
