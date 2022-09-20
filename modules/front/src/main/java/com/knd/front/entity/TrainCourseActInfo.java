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
 * @since 2020-07-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("train_course_act_info")
@ApiModel(value="TrainCourseActInfo对象", description="")
public class TrainCourseActInfo extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "训练课程block统计信息Id")
    private String trainCourseBlockInfoId;

    @ApiModelProperty(value = "动作Id")
    private String actId;

    @ApiModelProperty(value = "动作计数模式")
    private String actCountMod;

    @ApiModelProperty(value = "动作名称")
    private String action;

    @ApiModelProperty(value = "动作总时间")
    private String actTotalSeconds;

    @ApiModelProperty(value = "动作基础力设置")
    private String actBasePowerSetting;

    @ApiModelProperty(value = "动作完成组数")
    private String actFinishSets;

    @ApiModelProperty(value = "动作目标组数")
    private String actAimSets;

    @ApiModelProperty(value = "动作目标时间")
    private String actAimDuration;

    @ApiModelProperty(value = "动作总力")
    private String actTotalPower;

    @ApiModelProperty(value = "序号")
    private String sort;

}
