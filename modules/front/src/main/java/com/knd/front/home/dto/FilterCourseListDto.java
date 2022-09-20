package com.knd.front.home.dto;

import lombok.Data;

import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/2
 * @Version 1.0
 */
@Data
public class FilterCourseListDto {
    private String courseId;
    private String course;
    private String trainingFlag;
    private String videoAttachUrl;
    private String videoSize;
    private String picAttachUrl;
    private String sort;
    private String remark;
    private String videoDurationMinutes;
    private List<BaseCourseTypeDto> typeList;
    private List<BaseTargetDto> targetList;
    private List<BaseBodyPartDto> partList;
}