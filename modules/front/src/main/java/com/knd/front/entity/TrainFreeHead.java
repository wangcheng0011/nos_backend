package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 
 * </p>
 *
 * @author llx
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("train_free_head")
@ApiModel(value="TrainFreeHead对象", description="")
public class TrainFreeHead extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "会员Id")
    private String userId;

    @ApiModelProperty(value = "动作id")
    private String actId;

    @ApiModelProperty(value = "动作名称")
    private String action;

    @ApiModelProperty(value = "视频播放开始时间")
    private String vedioBeginTime;

    @ApiModelProperty(value = "视频播放结束时间")
    private String vedioEndTime;

    @ApiModelProperty(value = "运动总时间（s）")
    private String totalSeconds;

    @ApiModelProperty(value = "实际训练总时间（秒）")
    private String actTrainSeconds;

    @ApiModelProperty(value = "完成总组数")
    private String finishSets;

    @ApiModelProperty(value = "完成总次数")
    private String finishCounts;

    @ApiModelProperty(value = "完成总力")
    private String finishTotalPower;

    private String equipmentNo;
    @ApiModelProperty(value = "最大爆发力")
    private String maxExplosiveness;
    @ApiModelProperty(value = "平均爆发力")
    private String avgExplosiveness;
    @ApiModelProperty(value = "卡路里")
    private String calorie;
}
