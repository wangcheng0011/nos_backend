package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.knd.mybatis.BaseEntity;
import lombok.Data;

/**
 * 训练计划请假表 
 * 
 * @author wille
 * @email wille381@gmail.com
 * @date 2020-12-21 10:57:46
 */
@Data
@TableName("train_program_holiday")
public class ProgramHolidayEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 训练计划Id
	 */
	private String trainProgramId;
	/**
	 * 请假类型 0请假
	 */
	private String holidayType;
	/**
	 * 请假天数
	 */
	private String dayNum;


	/**
	 * 请假日期
	 */
	private LocalDateTime restDate;


}
