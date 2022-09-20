package com.knd.front.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zm
 */

@ApiModel(value = "频率类型")
@Getter
@AllArgsConstructor
public enum FrequencyEnum {

    /**
     * 不限
     */
    @ApiModelProperty("不限")
    NOLIMIT("不限"),

    /**
     * 1-2次/周
     */
    @ApiModelProperty("1-2次/周")
    ONETOTWO("1-2次/周"),

    /**
     * 3-4次/周
     */
    @ApiModelProperty("3-4次/周")
    THREETOFOUR("3-4次/周"),


    /**
     * 5-6次/周
     */
    @ApiModelProperty("5-6次/周")
    FIVETOSIX("5-6次/周");


    private final String display;


}
