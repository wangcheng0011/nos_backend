package com.knd.front.train.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * @author will
 */
@Data
public class TrainProgramPlanDto {


    @ApiModelProperty(value = "计划训练时间")
    private String trainDate;

    @ApiModelProperty(value = "当天训练明细")
    private Collection<ProgramPlanGenerationDto> programPlanGenerationDtoList;


}
