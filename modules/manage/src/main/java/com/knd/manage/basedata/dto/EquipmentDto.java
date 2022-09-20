package com.knd.manage.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EquipmentDto {
    @ApiModelProperty(value = "器材id")
    private String equipmentId;
    @ApiModelProperty(value = "器材名字")
    private String equipment;
}
