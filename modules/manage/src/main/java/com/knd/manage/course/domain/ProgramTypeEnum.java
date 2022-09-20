package com.knd.manage.course.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author zm
 */

@ApiModel(value = "训练计划类型")
public enum ProgramTypeEnum {

    /**
     * 专项增肌
     */
    @ApiModelProperty("专项增肌")
    BUILDMUSCLES("专项增肌"),

    @ApiModelProperty("有氧燃脂")
    GREASEBURNING("有氧燃脂"),

    @ApiModelProperty("大咖代练")
    BIGSHOT("大咖代练"),

    @ApiModelProperty("新手体验")
    NOVICE("新手体验");

    private String display;

    ProgramTypeEnum(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return this.display;
    }
}
