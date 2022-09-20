package com.knd.front.food.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class VoSaveFoodRecord {
    //userId从token获取
    private String userId;

    @Size(max = 64)
    @ApiModelProperty("食物记录id")
    private String id;

    @ApiModelProperty(value = "早餐实际摄入")
    private String breakfastIntake;

    @ApiModelProperty(value = "午餐实际摄入")
    private String lunchIntake;

    @ApiModelProperty(value = "晚餐实际摄入")
    private String dinnerIntake;

    @ApiModelProperty(value = "加餐实际摄入")
    private String supperIntake;

    @ApiModelProperty("1新增2修改")
    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;


}
