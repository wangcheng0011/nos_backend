package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.mybatis.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 训练计划 
 * 
 * @author wille
 * @email wille381@gmail.com
 * @date 2020-12-21 10:57:46
 */
@Data
@TableName("train_program")
public class ProgramEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 计划名称
	 */
	private String programName;
	/**
	 * 会员Id
	 */
	private String userId;
	/**
	 * 计划开始时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime beginTime;
	/**
	 * 计划结束时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime endTime;
	/**
	 * 计划训练天数
	 */
	private String trainTotalDayNum;
	/**
	 * 循环周数
	 */
	private String trainWeekNum;
	/**
	 * 每周训练天数
	 */
	private String trainWeekDayNum;

	/**
	 * 来源
	 */
	private String source;

	/**
	 * 计划类型
	 */
	private String type;

	/**
	 * 图片id
	 */
	private String picAttachId;

}
