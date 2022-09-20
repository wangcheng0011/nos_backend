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
 * @author sy
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_action")
@ApiModel(value="BaseAction对象", description="")
public class BaseAction extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "动作类型id")
    private String actionType;

    @ApiModelProperty(value = "动作名称")
    private String action;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "目标id")
    private String targetId;

    @ApiModelProperty(value = "部位id")
    private String partId;

    @ApiModelProperty(value = "计数模式")
    private String countMode;

    @ApiModelProperty(value = "默认计数力量等级")
    private String powerLevel;

    @ApiModelProperty(value = "默认计数目标时长")
    private String aimDuration;

    @ApiModelProperty(value = "默认计数目标次数")
    private String aimTimes;

    @ApiModelProperty(value = "默认基础力")
    private String basePower;

    @ApiModelProperty(value = "介绍视频id")
    private String videoAttachId;

    @ApiModelProperty(value = "封面图片id")
    private String picAttachId;

    @ApiModelProperty(value = "力量等级测试动作标识")
    private String strengthTestFlag;

    @ApiModelProperty(value = "动作标识")
    private String freeTrainFlag;

    @ApiModelProperty(value = "左臂左右角度")
    private String leftRightAngleOfLeft;

    @ApiModelProperty(value = "右臂左右角度")
    private String leftRightAngleOfRight;

    @ApiModelProperty(value = "左臂上下角度")
    private String upDownAngleOfLeft;

    @ApiModelProperty(value = "右臂上下角度")
    private String upDownAngleOfRight;

    @ApiModelProperty(value = "左臂上下高度")
    private String heightOfLeft;

    @ApiModelProperty(value = "右臂上下高度")
    private String heightOfRight;

    @ApiModelProperty(value = "是否双臂 0不添加 1单臂 2双臂")
    private String isTwoArms;


}
