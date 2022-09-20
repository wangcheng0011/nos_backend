package com.knd.front.user.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zm
 */
@Data
public class PowerTestResultDetailRequest {

    @ApiModelProperty(value = "力量测试子项ID")
    private String powerTestItemId;

    @ApiModelProperty(value = "测试时长(秒)")
    private String totalDuration;

    @ApiModelProperty(value = "完成标准值")
    private String finishedKpi;

    @ApiModelProperty(value = "完成总力")
    private String finishedPower;

    @ApiModelProperty(value = "排序")
    private String sort;
}
