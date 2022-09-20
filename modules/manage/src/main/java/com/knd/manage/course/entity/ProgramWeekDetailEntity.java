package com.knd.manage.course.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import lombok.Data;

/**
 * 训练计划周训练明细 
 * 
 * @author wille
 * @email wille381@gmail.com
 * @date 2020-12-21 10:57:46
 */
@Data
@TableName("train_program_week_detail")
public class ProgramWeekDetailEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 计划Id
	 */
	private String programId;
	/**
	 * 周几
	 */
	private String weekDayName;


}
