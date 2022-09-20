package com.knd.front.user.dto;


import com.knd.front.entity.BasePowerStandardUse;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;


/**
 * @author will
 */
@Data
public class PowerTestDto {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "难度图标")
    private String picUrl;
    @ApiModelProperty(value = "难度图标")
    private String difficultyName;
    @ApiModelProperty(value = "总时长（秒）")
    private String totalDuration;
    @ApiModelProperty(value = "排序")
    private String sort;
    @ApiModelProperty(value = "测试项")
    private List<PowerTestItemDto> items;


}