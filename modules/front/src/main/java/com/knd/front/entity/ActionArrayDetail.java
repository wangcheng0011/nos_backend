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
@TableName("action_array_detail")
@ApiModel(value="ActionArrayDetail对象", description="")
public class ActionArrayDetail extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "动作序列Id")
    private String actionArrayId;

    @ApiModelProperty(value = "动作Id")
    private String actionId;

    @ApiModelProperty(value = "计数目标次数")
    private String aimDuration;

    @ApiModelProperty(value = "计数目标时长")
    private String aimTimes;

    @ApiModelProperty(value = "排序")
    private String sort;


}
