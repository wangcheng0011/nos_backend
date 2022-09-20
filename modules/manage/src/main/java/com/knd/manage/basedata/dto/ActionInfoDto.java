package com.knd.manage.basedata.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "动作列表对象", description = "")
public class ActionInfoDto {
    @ApiModelProperty(value = "动作id")
    private String id;
    @ApiModelProperty(value = "目标名称")
    private String target;
    @ApiModelProperty(value = "部位名称")
    private String part;
    @ApiModelProperty(value = "动作名称")
    private String action;
    @ApiModelProperty(value = "动作类型")
    private String actionType;
    @ApiModelProperty(value = "动作类型名称")
    private String actionTypeName;
    @ApiModelProperty(value = "适宜器材，以逗号分隔字段")
    private String equipments;
    @ApiModelProperty(value = "计数模式")
    private String countMode;
    @ApiModelProperty(value = "默认计数目标时长")
    private String aimDuration;
    @ApiModelProperty(value = "默认计数目标次数")
    private String aimTimes;
    @ApiModelProperty(value = "是否双臂 0不添加 1单臂 2双臂")
    private String isTwoArms;

}
