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
public enum FollowStatusEnum {
    /**
     * 0->未关注；1->已关注;2->被关注；3->好友
     */
    NOT_FOLLOW("0","未关注"),
    IN_FOLLOW("1","已关注"),
    BE_FOLLOW("2","被关注"),
    FRIEND("3","好友");

    private final String code;
    private final String message;

}
