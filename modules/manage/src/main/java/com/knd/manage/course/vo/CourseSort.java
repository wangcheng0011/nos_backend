package com.knd.manage.course.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CourseSort {
    @NotBlank
    private String id;
    @NotBlank
    private String sort;
}
