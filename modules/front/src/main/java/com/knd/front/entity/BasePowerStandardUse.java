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
@TableName("base_power_standard_use")
@ApiModel(value="BasePowerStandardUse对象", description="")
public class BasePowerStandardUse extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "动作类型Id")
    private String actionType;

    @ApiModelProperty(value = "性别")
    private String gender;

    @ApiModelProperty(value = "体重")
    private String weight;

    @ApiModelProperty(value = "初级")
    private String elementary;

    @ApiModelProperty(value = "新手")
    private String novice;

    @ApiModelProperty(value = "中级")
    private String intermediate;

    @ApiModelProperty(value = "高级")
    private String senior;

    @ApiModelProperty(value = "精英")
    private String elite;
}
