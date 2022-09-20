package com.knd.front.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zm
 */

@ApiModel(value = "运动类型")
@Getter
@AllArgsConstructor
public enum SportEnum {
    /**
     * 总力量
     */
    @ApiModelProperty("有氧为主")
    AEROBIC("有氧为主"),

    /**
     * 毅力
     */
    @ApiModelProperty("无氧为主")
    ANAEROBIC("无氧为主"),

    /**
     * 爆发力
     */
    @ApiModelProperty("有氧&无氧")
    AEROBICANDANAEROBIC("有氧&无氧");


    private final String display;


}
