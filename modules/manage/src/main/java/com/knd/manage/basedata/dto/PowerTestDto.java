package com.knd.manage.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zm
 */
@Data
public class PowerTestDto {

    private String id;

    @ApiModelProperty(value = "性别 0男1女")
    private String gender;

    @ApiModelProperty(value = "难度Id")
    private String difficultyId;

    @ApiModelProperty(value = "总测试时长(秒)")
    private String totalDuration;

    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "图片")
    private ImgDto PicAttach;

    @ApiModelProperty(value = "子项明细")
    private List<PowerTestItemDto> itemDtoList;

}
