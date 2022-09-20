package com.knd.manage.course.dto;

import com.knd.manage.course.entity.BaseCourseType;
import lombok.Data;

import java.util.List;

@Data
public class CourseTypeListDto {
    //总数
    private int total;
    //列表
    private List<BaseCourseType> courseTypeList;

}
