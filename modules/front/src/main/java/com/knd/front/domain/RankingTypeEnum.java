package com.knd.front.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zm
 */
@SuppressWarnings("ALL")
@Getter
@AllArgsConstructor
@ApiModel(value = "排行榜类型")
public enum RankingTypeEnum {

    /**
     * 总力量
     */
    @ApiModelProperty("总力量")
    POWER("0","finishTotalPower"),

    /**
     * 毅力
     */
    @ApiModelProperty("毅力")
    WILL("1","actTrainSeconds"),

    /**
     * 爆发力
     */
    @ApiModelProperty("爆发力")
    MAXPOWER("2","maxExplosiveness"),

    /**
     * 卡路里
     */
    @ApiModelProperty("卡路里")
    CALORIE("3","calorie");

    private final String praiseType;
    private final String paramType;
}
