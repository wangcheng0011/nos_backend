package com.knd.front.train.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 训练计划生成表 
 * 
 * @author wille
 * @email wille381@gmail.com
 * @date 2020-12-21 10:57:46
 */
@Data
public class ProgramPlanGenerationDto {
	/**
	 * Id
	 */
	@ApiModelProperty(value = "id")
	private String id;

	/**
	 * 计划Id
	 */
	@ApiModelProperty(value = "计划Id")
	private String trainProgramId;
	/**
	 * 会员Id
	 */
	@ApiModelProperty(value = "会员Id")
	private String userId;
	/**
	 * 计划训练时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@ApiModelProperty(value = "会员Id")
	private String trainDate;
	/**
	 * 计划训练项目类型 0课程1动作序列
	 */
	@ApiModelProperty(value = "计划训练项目类型 0课程1动作序列")
	private String trainItemType;
	/**
	 * 计划训练项目Id
	 */
	@ApiModelProperty(value = "计划训练项目Id")
	private String trainItemId;

	@ApiModelProperty(value = "计划训练项目名称")
	private String trainItemName;

	@ApiModelProperty(value = "视频总时长")
	private String videoDuration;

	@ApiModelProperty(value = "计划训练项目名称")
	private CourseHeadDto courseHead;

	/**
	 * 训练完成标识 0未完成1完成
	 */
	@ApiModelProperty(value = "训练完成标识 0未完成1完成")
	private String trainFinishFlag;

	/**
	 * 训练记录Id
	 */
	@ApiModelProperty(value = "训练记录Id")
	private String trainHeadId;

	/**
	 * 动作序列
	 */
	@ApiModelProperty(value = "动作序列")
	private ActionArrayDto actionArrayDto;



}
