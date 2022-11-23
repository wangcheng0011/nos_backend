package com.knd.front.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author will
 */
@Data
public class TrainProgramDetailDto {

    @ApiModelProperty(value = "计划id")
    private String id;

    @ApiModelProperty(value = "计划类型：0专项增肌 1有氧燃脂 2大咖代练 3新手体验")
    private String type;

    @ApiModelProperty(value = "计划名称")
    private String programName;

    @ApiModelProperty(value = "循环周数")
    private String trainWeekNum;

    private List<TrainProgramWeekDetailDto> trainProgramWeekDetail;

}
