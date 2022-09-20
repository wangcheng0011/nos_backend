package com.knd.manage.basedata.entity;

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
@TableName("base_action_aim")
@ApiModel(value="BaseActionAim对象", description="")
public class BaseActionAim extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "动作id")
    private String action;

    @ApiModelProperty(value = "计数力量等级")
    private String powerLevel;

    @ApiModelProperty(value = "计数目标时长")
    private String aimDuration;

    @ApiModelProperty(value = "计数目标次数")
    private String aimTimes;

    @ApiModelProperty(value = "默认基础力")
    private String basePower;


}
