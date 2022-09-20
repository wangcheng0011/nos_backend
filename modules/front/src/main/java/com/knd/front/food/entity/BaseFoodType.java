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
@TableName("base_food_type")
@ApiModel(value="BaseFoodType对象", description="")
public class BaseFoodType extends BaseEntity {

    private static final long serialVersionUID=1L;
    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "食物分类图片id")
    private String foodUrlId;



}
