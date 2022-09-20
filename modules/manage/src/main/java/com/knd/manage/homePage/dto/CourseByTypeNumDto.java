package com.knd.manage.homePage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author zm
 */
@Data
public class CourseByTypeNumDto {

    @ApiModelProperty(value = "自由训练每日数量")
    private List<Map<String, Object>> freeTrainingTotal;

    @ApiModelProperty(value = "自由训练增长")
    private String freeTrainingTotalGrowth;

    @ApiModelProperty(value = "系列课程数量")
    private List<Map<String, Object>>  seriesCourseTotal;

    @ApiModelProperty(value = "系列课程数量增长")
    private String seriesCourseTotalGrowth;

    @ApiModelProperty(value = "课程数量")
    private List<Map<String, Object>>  courseTotal;

    @ApiModelProperty(value = "课程增长")
    private String courseTotalGrowth;

    @ApiModelProperty(value = "计划数量")
    private List<Map<String, Object>>  planTotal;

    @ApiModelProperty(value = "计划数量增长")
    private String planTotalGrowth;

}
