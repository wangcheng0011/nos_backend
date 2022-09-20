package com.knd.manage.homePage.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import lombok.Data;

/**
 * 训练计划训练主信息表 
 * 
 * @author wille
 * @email wille381@gmail.com
 * @date 2020-12-21 10:57:46
 */
@Data
@TableName("train_program_train_head")
public class ProgramHeadEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 对应训练计划Id
	 */
	private String planGenerationId;
	/**
	 * 对应训练记录Id
	 */
	private String trainHeadInfoId;
	/**
	 * 会员id
	 */
	private String userId;


}
