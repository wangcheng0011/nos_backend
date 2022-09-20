package com.knd.manage.course.domain;

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
    @ApiModelProperty("普通课程")
    ORDINARY("普通课程"),

    @ApiModelProperty("特色课程")
    CHARACTERISTIC("特色课程");

    private String display;

    CourseTypeEnum(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return this.display;
    }
}
