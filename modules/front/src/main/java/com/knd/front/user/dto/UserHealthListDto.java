package com.knd.front.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserHealthListDto {

    @ApiModelProperty(value = "体重列表",required = true)
    private List<HealthByDateDto> currentWeightList;

    @ApiModelProperty(value = "身高列表",required = true)
    private List<HealthByDateDto> heightList;

    @ApiModelProperty(value = "胸围列表",required = true)
    private List<HealthByDateDto> bustList;

    @ApiModelProperty(value = "腰围列表",required = true)
    private List<HealthByDateDto> waistList;

    @ApiModelProperty(value = "臀围列表",required = true)
    private List<HealthByDateDto> hiplineList;

    @ApiModelProperty(value = "臂围列表",required = true)
    private List<HealthByDateDto> armCircumferenceList;
}
