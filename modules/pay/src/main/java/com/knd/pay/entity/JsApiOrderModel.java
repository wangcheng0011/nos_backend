package com.knd.pay.entity;
 
 
import com.ijpay.core.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
/**
 * @Author: Hello World
 * @Date: 2021/5/18 10:46
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JsApiOrderModel extends BaseModel {
    private String appid;
    private String sub_appid;
    private String mch_id;
    private String sub_mch_id;
    private String nonce_str;
    private String sign;
    private String body;
    private String attach;
    private String out_trade_no;
    private String fee_type;
    private String total_fee;
    private String spbill_create_ip;
    private String time_start;
    private String time_expire;
    private String limit_pay;
    private String contract_code;
    private String promotion_tag;
    private String trade_type;
    private String notify_url;
    private String device_info;
    private String mini_app_param;
    private String openid;
}