package com.knd.manage.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseFloorDto {
    private String id;

    @ApiModelProperty(value = "key值")
    private String keyValue;

    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelProperty(value = "楼层类型")
    private String floorType;

    @ApiModelProperty(value = "后台展示图")
    private String showUrl;

    @ApiModelProperty(value = "后台展示说明")
    private String description;
}
