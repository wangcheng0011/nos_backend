package com.knd.pay.entity;

import com.ijpay.wxpay.model.v3.Amount;
import com.ijpay.wxpay.model.v3.Payer;
import lombok.Data;

@Data
public class PrepayIdRequest {
    private String mchid;
    private String out_trade_no;
    private String appid;
    private String description;
    private String notify_url;
    private Amount amount;
    private Payer payer;



}
