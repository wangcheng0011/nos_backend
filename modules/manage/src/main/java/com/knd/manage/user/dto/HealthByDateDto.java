package com.knd.manage.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HealthByDateDto {
    @ApiModelProperty(value = "日期")
    private LocalDate date;

    @ApiModelProperty(value = "值")
    private String value;
}
