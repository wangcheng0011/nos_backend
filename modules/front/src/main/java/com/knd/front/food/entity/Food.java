package com.knd.front.food.entity;

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
 * @author wangcheng
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("food_diet")
@ApiModel(value="dietFood对象", description="")
public class Food extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "食物分类id")
    private String parentId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "数量")
    private String quantity;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "热量 单位:大卡")
    private String quantityHeat;




}
