package com.knd.manage.homePage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author zm
 */
@Data
public class CourseByTargetNumDto {

    @ApiModelProperty(value = "减脂课程每日数量")
    private List<Map<String, Object>> fatReductionTotal;

    @ApiModelProperty(value = "减脂日增长")
    private String fatReductionTotalGrowth;

    @ApiModelProperty(value = "塑形课程数量")
    private List<Map<String, Object>>  shapingTotal;

    @ApiModelProperty(value = "塑形课程数量增长")
    private String shapingTotalGrowth;

    @ApiModelProperty(value = "增肌课程数量")
    private List<Map<String, Object>>  buildMusclesTotal;

    @ApiModelProperty(value = "增肌课程增长")
    private String buildMusclesGrowth;

    @ApiModelProperty(value = "系列课程数量")
    private List<Map<String, Object>>  seriesCourseTotal;

    @ApiModelProperty(value = "系列课程增长")
    private String seriesCourseTotalGrowth;

}
