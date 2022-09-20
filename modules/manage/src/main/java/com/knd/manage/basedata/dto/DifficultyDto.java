package com.knd.manage.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
public class DifficultyDto {

    private String id;

    @ApiModelProperty(value = "类别")
    private String type;

    @ApiModelProperty(value = "难度")
    private String difficulty;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "图片")
    private ImgDto imageUrl;

}
