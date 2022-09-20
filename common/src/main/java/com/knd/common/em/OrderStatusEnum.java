package com.knd.common.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * order状态
 * @author will
 */
@SuppressWarnings("ALL")
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    /**
     * 1、未付款/待支付,2、已支付/待发货,3已发货,4已完成，5已取消,6退款中,7已退款
     */
    WAIT_FOR_PAY("1","待支付"),
    PAY_SUCCESS("2","待发货"),
    DELIVER_FINISHED("3","已发货"),

    ORDER_FINISHED("4","已完成"),
    ORDER_CLOSED("5","已取消"),
    REFUNDING("6","退款中"),
    REFUNDED("7","已退款"),

    ;

    private final String code;
    private final String message;

}
