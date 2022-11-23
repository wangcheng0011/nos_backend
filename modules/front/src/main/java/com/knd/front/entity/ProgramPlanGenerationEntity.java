package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.mybatis.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 训练计划生成表 
 * 
 * @author wille
 * @email wille381@gmail.com
 * @date 2020-12-21 10:57:46
 */
@Data
@TableName("train_program_plan_generation")
public class ProgramPlanGenerationEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 计划Id
	 */
	private String trainProgramId;
	/**
	 * 会员Id
	 */
	private String userId;
	/**
	 * 计划训练时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private LocalDateTime trainDate;
	/**
	 * 计划训练项目类型 0课程1动作序列
	 */
	private String trainItemType;
	/**
	 * 计划训练项目Id
	 */
	private String trainItemId;
	/**
	 * 训练完成标识 0未完成1完成2请假
	 */
	private String trainFinishFlag;
	/**
	 * 所属周数
	 */
	private String trainWeekNum;
	/**
	 * 周几
	 */
	private String dayName;


}
