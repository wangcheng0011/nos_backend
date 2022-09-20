package com.knd.manage.food.entity;

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
@TableName("food_record")
@ApiModel(value="foodRecord对象", description="")
public class FoodRecord extends BaseEntity {

    private static final long serialVersionUID=1L;
    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "已摄入")
    private String alreadyIntake;

    @ApiModelProperty(value = "还可以吃")
    private String canAlsoEat;

    @ApiModelProperty(value = "推荐摄入")
    private String recommendIntake;

    @ApiModelProperty(value = "已消耗")
    private String alreadyConsume;


    @ApiModelProperty(value = "早餐实际摄入")
    private String breakfastIntake;


    @ApiModelProperty(value = "午餐实际摄入")
    private String lunchIntake;


    @ApiModelProperty(value = "晚餐实际摄入")
    private String dinnerIntake;

    @ApiModelProperty(value = "加餐实际摄入")
    private String supperIntake;





}
