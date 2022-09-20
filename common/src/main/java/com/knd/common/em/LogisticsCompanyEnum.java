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
public enum LogisticsCompanyEnum {
    DEPPON("1","德邦"),
    ANE("2","安能"),
    ;

    private final String code;
    private final String message;

}
