package com.knd.manage.website.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


/**
 * @author wangcheng
 */
@Data
@TableName("pms_news")
public class NewsEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "id")
	private String id;

	@ApiModelProperty(value = "是否推荐")
	private String recommend;

	@ApiModelProperty(value = "分类")
	private String classify;

	@ApiModelProperty(value = "图片附件id")
	private String attachId;

	@ApiModelProperty(value = "标题")
	private String title;

	@ApiModelProperty(value = "描述")
	private String represent;

	@ApiModelProperty(value = "内容")
	private String content;

	@ApiModelProperty(value = "阅读数")
	private Integer readCount;

	@ApiModelProperty(value = "发布时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime publishTime;



}
