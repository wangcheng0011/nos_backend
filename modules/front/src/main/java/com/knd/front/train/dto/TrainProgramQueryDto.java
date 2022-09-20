package com.knd.front.train.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @author will
 */
@Data
public class TrainProgramQueryDto {


    @ApiModelProperty(value = "主键ID")
    protected String id;

    @ApiModelProperty(value = "计划训练名称")
    private String programName;

    @ApiModelProperty(value = "计划训练开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  //  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "计划训练结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  //  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "计划训练明细")
    private Collection<TrainProgramPlanDto> trainProgramPlanDtoList;


}
