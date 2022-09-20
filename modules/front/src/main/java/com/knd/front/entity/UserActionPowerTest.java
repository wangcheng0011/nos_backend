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
@TableName("user_action_power_test")
@ApiModel(value="UserActionPowerTest对象", description="")
public class UserActionPowerTest extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "会员id")
    private String userId;

    @ApiModelProperty(value = "动作类型Id")
    private String actionType;

    @ApiModelProperty(value = "动作类型名称")
    private String actionTypeName;

    @ApiModelProperty(value = "动作Id")
    private String actionId;

    @ApiModelProperty(value = "动作名称")
    private String actionName;


    @ApiModelProperty(value = "最大测试力")
    private String testPower;

    @ApiModelProperty(value = "平均测试力")
    private String avgPower;

    @ApiModelProperty(value = "测试级别")
    private String powerLevel;

}
