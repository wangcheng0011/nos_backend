package com.knd.pay.entity;

import lombok.Data;

@Data
public class AmountBean {
    /**
     * 总金额
     */
    private int total;
    /**
     * 货币类型
     */
    private String currency;
}
