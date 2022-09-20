package com.knd.common.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付类型
 * @author will
 */
@SuppressWarnings("ALL")
@Getter
@AllArgsConstructor
public enum PaymentTypeEnum {
    ALIPAY("1","支付宝"),
    WXPAY("2","微信"),
    UNIONPAY("3","银联"),
    OFFLINE("4","线下付款"),
    ;

    private final String code;
    private final String message;

}
