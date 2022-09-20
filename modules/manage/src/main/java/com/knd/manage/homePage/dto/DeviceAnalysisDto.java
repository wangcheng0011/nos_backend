package com.knd.manage.homePage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zm
 */
@Data
public class DeviceAnalysisDto {

    @ApiModelProperty(value = "自由训练数量")
    private String freeTrainingNum;

    @ApiModelProperty(value = "课程数量")
    private String courseNum;

    @ApiModelProperty(value = "训练计划数量")
    private String planNum;

    @ApiModelProperty(value = "系列课程数量")
    private String seriesCourseNum;

    @ApiModelProperty(value = "直播数量")
    private String liveNum;
}
