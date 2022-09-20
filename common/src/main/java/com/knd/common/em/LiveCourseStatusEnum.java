package com.knd.common.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 直播状态
 * @author will
 */
@SuppressWarnings("ALL")
@Getter
@AllArgsConstructor
public enum LiveCourseStatusEnum {

    LIVE_NOT_START("0","未开始"),
    LIVE_ING("1","直播中"),
    LIVE_FINISHED("2","已完成"),
    LIVE_CANCEL("3","已取消"),


    ;

    private final String code;
    private final String message;

}
