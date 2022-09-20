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
public enum LiveOrderStatusEnum {

    LIVE_ING("0","预约中"),
    LIVE_FINISHED("1","已预约"),
    LIVE_CANCEL("2","已取消"),


    ;

    private final String code;
    private final String message;

}
