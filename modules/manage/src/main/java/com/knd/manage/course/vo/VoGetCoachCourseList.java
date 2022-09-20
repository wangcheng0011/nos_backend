package com.knd.manage.course.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * @author Lenovo
 */
@Data
public class VoGetCoachCourseList {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String date;
//    @NotBlank
    @ApiModelProperty(value = "教练用户id")
    private String coachUserId;

    private String current;
}
