package com.knd.front.social.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class RecordMessageDto {

    @ApiModelProperty(value = "类型：健身，跑步，行走，骑行")
    private String type;

    @ApiModelProperty(value = "总运动时长/总距离")
    private String record1  = "0";

    @ApiModelProperty(value = "总消耗/最远距离")
    private String record2  = "0";

}
