package com.knd.manage.course.dto;

import lombok.Data;

import java.util.List;

@Data
public class CourseHeadListDto {
    //总数
    private int total;
    //列表
    private List<CourseHeadInfoDto> courseHeadList;
}
