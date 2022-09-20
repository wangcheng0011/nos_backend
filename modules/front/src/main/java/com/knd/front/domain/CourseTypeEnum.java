package com.knd.front.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author zm
 */

@ApiModel(value = "课程类型")
public enum CourseTypeEnum {

    /**
     * 普通课程
     */
    @ApiModelProperty("热门课程")
    ORDINARY("热门课程"),

    @ApiModelProperty("专享课程")
    CHARACTERISTIC("专享课程");

    private String display;

    CourseTypeEnum(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return this.display;
    }
}
