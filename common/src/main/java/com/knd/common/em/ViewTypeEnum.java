package com.knd.common.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 浏览类型 1自由训练2课程3训练计划4系列课程5直播
 * @author will
 */
@SuppressWarnings("ALL")
@Getter
@AllArgsConstructor
public enum ViewTypeEnum {

    FREE_TRAIN(1,"自由训练"),
    COURSE(2,"课程"),
    TRAIN_PROGRAM(3,"训练计划"),
    SERIES_COURSE(4,"系列课程"),
    LIVE(5,"直播"),
    ;

    private final Integer code;
    private final String message;

}
