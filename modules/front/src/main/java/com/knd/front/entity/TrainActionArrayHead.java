package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author will
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("train_action_array_head")
@ApiModel(value="TrainActionArrayHead对象", description="")
public class TrainActionArrayHead extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "会员Id")
    private String userId;

    @ApiModelProperty(value = "动作序列id")
    private String actionArrayId;

    @ApiModelProperty(value = "动作序列名称")
    private String actionArrayName;

    @ApiModelProperty(value = "训练开始时间")
    private String trainBeginTime;

    @ApiModelProperty(value = "训练结束时间")
    private String trainEndTime;

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
