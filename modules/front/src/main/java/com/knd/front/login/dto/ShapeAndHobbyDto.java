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
public class ShapeAndHobbyDto {

    @ApiModelProperty(value = "爱好")
    private List<HobbyDto> hobbyList;

    @ApiModelProperty(value = "男性体型")
    private List<ShapeDto> manShapeList;

    @ApiModelProperty(value = "女性体型")
    private List<ShapeDto> womanShapeList;

}
