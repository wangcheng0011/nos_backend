package com.knd.front.entity;

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
@TableName("train_user_praise")
public class TrainUserPraiseEntity extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 会员id
	 */
	private String userId;
	/**
	 * 被点赞会员Id
	 */
	private String praiseUserId;

	/**
	 * 点赞状态 0点赞 1取消
	 */
	private String praise;

	/**
	 * 点赞类型 0总力量 1毅力 2爆发力'
	 */
	private String praiseType;

}
