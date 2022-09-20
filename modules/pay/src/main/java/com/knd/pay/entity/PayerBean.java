package com.knd.pay.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PayerBean {
    /**
     * 用户标识
     */
    private String openid;
    /**
     * 用户服务标识
     */
    private String sp_openid;
    /**
     * 用户子标识
     */
    private String sub_openid;
}