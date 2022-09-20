package com.knd.manage.course.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class VoDeleteCourseBlock {
    private String userId;
    @NotBlank
    @Size(max = 64)
    private String blockId;

}
