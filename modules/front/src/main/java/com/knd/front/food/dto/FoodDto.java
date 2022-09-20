package com.knd.front.food.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FoodDto {

   @ApiModelProperty("食物id")
    private String id;

    @ApiModelProperty(value = "食物分类id")
    private String parentId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "数量")
    private String quantity;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "热量")
    private String quantityHeat;



}
