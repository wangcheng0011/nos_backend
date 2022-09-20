package com.knd.front.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author will
 */
@Data
@Builder
public class TrainProgramWeekDetailDto {

    @ApiModelProperty(value = "周训练日期")
    private String weekDayName;

    private List<UserCoursePayDto> trainCourseList;


}
