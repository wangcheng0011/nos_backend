package com.knd.front.home.dto;

import lombok.Data;

import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/1
 * @Version 1.0
 */
@Data
public class CourseListDto {
    private String typeId;
    private String type;
    private List<CourseDto> typeCourseList;
    private String sort;
}