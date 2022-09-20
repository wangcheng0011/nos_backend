package com.knd.manage.food.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class VoSaveFoodItem {

    @Size(max = 64)
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
