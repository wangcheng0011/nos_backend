package com.knd.manage.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zm
 */
@Data
public class PowerTestListDto {

    private String id;

    @ApiModelProperty(value = "性别 0男1女")
    private String gender;

    @ApiModelProperty(value = "难度")
    private String difficulty;

    @ApiModelProperty(value = "总测试时长(秒)")
    private String totalDuration;

    @ApiModelProperty(value = "排序")
    private String sort;

}
