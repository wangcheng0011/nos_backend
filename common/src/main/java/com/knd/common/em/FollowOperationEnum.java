package com.knd.common.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * order状态
 * @author zm
 */
@SuppressWarnings("ALL")
@Getter
@AllArgsConstructor
public enum FollowOperationEnum {
    /**
     * 0->未关注；1->已关注;2->被关注；3->好友
     */
    FOLLOW("0","关注"),
    PASS("1","取关"),
    BACK_FOLLOW("2","回关"),
    DELETE("3","移除");

    private final String code;
    private final String message;

}
