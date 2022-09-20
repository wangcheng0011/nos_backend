package com.knd.front.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
public class TrainWeekDetailDto {
    @ApiModelProperty(value = "周训练日期")
    private String weekDayName;

    @ApiModelProperty(value = "训练项目类型 0课程 1动作序列")
    private String itemType;

    @ApiModelProperty(value = "训练项目类型名称 0课程 1动作序列")
    private String typeName;

    @ApiModelProperty(value = "训练项目Id")
    private String itemId;

    @ApiModelProperty(value = "训练项目")
    private String itemName;

    @ApiModelProperty(value = "周训练日期数字")
    private Long weekdaynum;

}
