package com.knd.manage.course.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author zm
 */

@ApiModel(value = "系列课程难度")
public enum SeriesDifficultyEnum {

    /**
     * 新手
     */
    @ApiModelProperty("新手")
    NOVICE("新手"),

    @ApiModelProperty("初级")
    PRIMARY("初级"),

    @ApiModelProperty("中级")
    INTERMEDIATE("中级"),

    @ApiModelProperty("高级")
    SENIOR("高级"),

    @ApiModelProperty("精英")
    ELITE("精英");

    private String display;

    SeriesDifficultyEnum(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return this.display;
    }
}
