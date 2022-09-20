package com.knd.front.train.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author zm
 */

@ApiModel(value = "训练计划类型")
public enum ProgramTypeEnum {

    @ApiModelProperty("专项增肌")
    BUILDMUSCLES,

    @ApiModelProperty("有氧燃脂")
    GREASEBURNING,

    @ApiModelProperty("大咖代练")
    BIGSHOT,

    @ApiModelProperty("新手体验")
    NOVICE;
}
