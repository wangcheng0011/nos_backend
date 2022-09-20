package com.knd.front.social.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zm
 */
@Data
public class LabelDto {

    @ApiModelProperty(value = "标签类别")
    private String type;

    @ApiModelProperty(value = "标签名")
    private String label;

}
