package com.knd.common.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * role
 * @author will
 */
@SuppressWarnings("ALL")
@Getter
@AllArgsConstructor
public enum RoleEnum {
    CUSTOMER_SERVICE("1","客服"),
    SALE("2","销售"),
    ERECTOR("3","技师"),
    DOMESTIC_CONSUMER("4","普通用户")
    ;

    private final String code;
    private final String name;

}
