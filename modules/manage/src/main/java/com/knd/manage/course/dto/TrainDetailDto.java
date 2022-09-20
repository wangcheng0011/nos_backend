package com.knd.manage.course.dto;

import com.knd.manage.basedata.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
public class TrainDetailDto {

    private String id;

    @ApiModelProperty(value = "计划名称")
    private String programName;

    @ApiModelProperty(value = "循环周数")
    private String trainWeekNum;

    @ApiModelProperty(value = "计划类型")
    private String type;

    @ApiModelProperty(value = "封面图片")
    private ImgDto picAttach;

    private List<TrainWeekDetailDto> trainDetailList;
}
