package com.knd.manage.course.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import lombok.Data;

/**
 * 训练计划日训练项 
 * 
 * @author wille
 * @email wille381@gmail.com
 * @date 2020-12-21 10:57:46
 */
@Data
@TableName("train_program_day_item")
public class ProgramDayItemEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 训练计划周明细Id
	 */
	private String trainProgramWeekDetailId;
	/**
	 * 训练项目类型 0课程1动作序列
	 */
	private String itemType;
	/**
	 * 训练项目Id
	 */
	private String itemId;


}
