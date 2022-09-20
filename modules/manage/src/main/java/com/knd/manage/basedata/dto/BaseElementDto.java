package com.knd.manage.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseElementDto {

    private String id;

    @ApiModelProperty(value = "key值")
    private String keyValue;

    @ApiModelProperty(value = "显示顺序")
    private String sort;

    private String description;

    @ApiModelProperty(value = "后台展示图")
    private String showUrl;

}
