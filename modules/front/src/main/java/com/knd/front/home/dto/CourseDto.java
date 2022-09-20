package com.knd.front.home.dto;

import lombok.Data;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/2
 * @Version 1.0
 */
@Data
public class CourseDto {
    private String courseId;
    private String course;
    private String trainingFlag;
    private String videoAttachUrl;
    private String picAttachUrl;
    private String chSort;
    private String lastModifiedDate;
}