package com.knd.front.login.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
@Builder
public class SportAndFrequencyDto {

    @ApiModelProperty(value = "运动方式")
    private List<SportDto> sportList;

    @ApiModelProperty(value = "运动频率")
    private List<FrequencyDto> frequencyList;
}
