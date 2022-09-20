package com.knd.common.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型
 * @author will
 */
@SuppressWarnings("ALL")
@Getter
@AllArgsConstructor
public enum OrderTypeEnum {
    /**
     * 订单类型；1-vip购买2-商品购买3-课程购买4-机器购买5-直播预约
     */
    VIP("1","vip购买"),
    GOODS("2","商品购买"),
    COURSE("3","课程购买"),

    EQUIPMENT("4","机器购买"),
    LIVE("5","直播预约"),

    ;

    private final String code;
    private final String message;

}
