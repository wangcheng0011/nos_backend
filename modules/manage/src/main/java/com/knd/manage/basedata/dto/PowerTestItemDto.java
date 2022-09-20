package com.knd.manage.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zm
 */
@Data
public class PowerTestItemDto {

    @ApiModelProperty(value = "动作ID")
    private String actionId;

    @ApiModelProperty(value = "部位ID")
    private String bodyPartId;

    @ApiModelProperty(value = "动作名称")
    private String actionName;

    @ApiModelProperty(value = "是否使用器材 0否 1是")
    private String useEquipmentFlag;

    @ApiModelProperty(value = "力量值")
    private String power;

    @ApiModelProperty(value = "标准值")
    private String kpa;

    @ApiModelProperty(value = "测试时长(秒)")
    private String duration;

    @ApiModelProperty(value = "排序")
    private String sort;
}
