package com.knd.common.constant;

/**
 * @author will
 * 常量
 * @date 2021年07月28日 13:50
 */
public class Constant {
    //七牛录像回看url
    public final static String QINIU_PILI_VOD_DOMAIN = "http://pili-vod.quinnoid.com/";
    //支付宝支付回调url
    public final static String ALI_NOTIFY_URL = "/pay/pay/notify_url";
    //wx支付回调url
    public final static String WX_NOTIFY_URL = "/pay/pay/wx_notify_url";
    //wx支付退款回调url
    public final static String WX_REFUND_URL = "/pay/pay/wx_refund_url";
    //订单支付超时时间（单位分钟）
    public final static Integer PAY_TIME_OUT = 30;
}
