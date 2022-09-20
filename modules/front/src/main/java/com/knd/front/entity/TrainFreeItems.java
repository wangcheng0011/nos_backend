package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author llx
 * @since 2020-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("train_free_items")
@ApiModel(value="TrainFreeItems对象", description="")
public class TrainFreeItems extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "动作主表Id")
    private String trainFreeHeadId;

    @ApiModelProperty(value = "动作Id")
    private String actId;

    @ApiModelProperty(value = "动作计数模式")
    private String actCountMod;

    @ApiModelProperty(value = "动作名称")
    private String action;

    @ApiModelProperty(value = "动作组序号")
    private String actSetNum;

    @ApiModelProperty(value = "动作训练时间")
    private String actTotalSeconds;

    @ApiModelProperty(value = "动作最后设置力")
    private String actLastPowerSetting;

    @ApiModelProperty(value = "动作完成数")
    private String actFinishCounts;

    @ApiModelProperty(value = "动作目标数")
    private String actAimCounts;

    @ApiModelProperty(value = "动作目标时间")
    private String actAimDuration;

    @ApiModelProperty(value = "完成总力")
    private String finishTotalPower;


}
