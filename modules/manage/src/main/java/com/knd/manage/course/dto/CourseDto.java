package com.knd.manage.course.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
public class CourseDto {

    private String id;
    private String course;

    private List<String> typeList;
    private List<String> targetList;
    private List<String> partList;
}
