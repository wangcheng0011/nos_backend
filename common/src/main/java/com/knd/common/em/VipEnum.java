package com.knd.common.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * vip
 * @author will
 */
@SuppressWarnings("ALL")
@Getter
@AllArgsConstructor
public enum VipEnum {
    ORDINARY_VIP("0","普通会员"),
    INDIVIDUAL("1","个人会员"),
    FAMILY_MASTER("2","家庭会员-主"),
    FAMILY_SLAVE("3","家庭会员-副")
    ;

    private final String code;
    private final String message;

}
