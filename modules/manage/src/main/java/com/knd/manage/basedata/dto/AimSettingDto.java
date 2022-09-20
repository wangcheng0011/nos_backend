package com.knd.manage.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AimSettingDto {
    @ApiModelProperty(value = "计数力量等级")
    private String powerLevel;
    @ApiModelProperty(value = "计数目标时长")
    private String aimDuration;
    @ApiModelProperty(value = "计数目标次数")
    private String aimTimes;
    @ApiModelProperty(value = "基础力")
    private String basePower;
}
