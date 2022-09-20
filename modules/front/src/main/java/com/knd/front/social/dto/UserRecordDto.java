package com.knd.front.social.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class UserRecordDto {

    @ApiModelProperty(value = "卡路里")
    private String totalCalorie  = "0";

    @ApiModelProperty(value = "总运动时长")
    private String totalTrainSeconds  = "0";

    @ApiModelProperty(value = "本月运动时长")
    private String monthTrainSeconds  = "0";

    @ApiModelProperty(value = "加入时间")
    private String registTime;

    @ApiModelProperty(value = "分类训练明细")
    List<RecordMessageDto> recordMessageDtoList;
}
