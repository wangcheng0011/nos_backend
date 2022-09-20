package com.knd.manage.user.entity;

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
 * @since 2020-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_power_level_test")
@ApiModel(value="UserPowerLevelTest对象", description="")
public class UserPowerLevelTest extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "会员id")
    private String userId;

    @ApiModelProperty(value = "动作Id")
    private String actId;

    @ApiModelProperty(value = "动作名称")
    private String action;

    @ApiModelProperty(value = "动作完成数")
    private String actFinishCounts;

    @ApiModelProperty(value = "测试总力")
    private String finishTotalPower;

    @ApiModelProperty(value = "单次最大力")
    private String maxPower;

    @ApiModelProperty(value = "力量等级")
    private String powerLevel;

    @ApiModelProperty(value = "力量等级版本号")
    private Integer version;


}
