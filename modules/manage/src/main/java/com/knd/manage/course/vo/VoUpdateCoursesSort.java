package com.knd.manage.course.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class VoUpdateCoursesSort {
    private String userId;
    @NotEmpty
    private List<CourseSort> courseSortList;

}
