package com.knd.pay.config.aliPay;

import com.knd.pay.entity.AliPayBean;

import javax.annotation.Resource;

public class AliPayConfig {
    @Resource
    private AliPayBean aliPayBean;


    //应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String APP_ID="2018121562XXXXX";
    //商户私钥，您的PKCS8格式RSA2私钥
    public static String MERCHANT_PRIVATE_KEY="MIIEvQIBADANxxxxxxxxxxxxx";
    //支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiGxxxxxxxxxxxxxxxxx";
    //服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String NOTIFY_URL="http://127.0.0.1:8089/alipay/notify";
    //页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String RETURN_URL="http://127.0.0.1:8089/alipay/return";
    //签名方式
    public static String SIGN_TYPE="RSA2";
    //字符编码格式
    public static String CHARSET="utf-8";
    //支付宝网关(沙盒环境)
    public static String GATEWAY_URL="https://openapi.alipaydev.com/gateway.do";















}
