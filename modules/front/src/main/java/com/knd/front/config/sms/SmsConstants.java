package com.knd.front.config.sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/8/6
 * @Version 1.0
 */
@Component
public class SmsConstants {
    /**
     * 短信发送apiURI
     */
    public static String url ;

    /**
     * APP_Key
     */
    public static String appKey  ;

    /**
     * APP_Secret
     */
    public static String appSecret  ;


    /**
     * 国内短信签名通道号或国际/港澳台短信通道号
     */
    public static String sender ;

    /**
     * 重置密码模板ID
     */
    public static String resetTemplateId  ;
    /**
     * 注册模板id
     */
    public static String registerTemplateId ;

    /**
     * 收货确认模板id
     */
    public static String receivingCodeTemplateId;

    /**
     * 登录验证码模板id
     */
    public static String loginCodeTemplateId;


    public static String getUrl() {
        return url;
    }
    @Value("${msgSms.url}")
    public void setUrl(String url) {
        SmsConstants.url = url;
    }

    public static String getAppKey() {
        return appKey;
    }
    @Value("${msgSms.appKey}")
    public void setAppKey(String appKey) {
        SmsConstants.appKey = appKey;
    }

    public static String getAppSecret() {
        return appSecret;
    }
    @Value("${msgSms.appSecret}")
    public void setAppSecret(String appSecret) {
        SmsConstants.appSecret = appSecret;
    }

    public static String getSender() {
        return sender;
    }
    @Value("${msgSms.sender}")
    public void setSender(String sender) {
        SmsConstants.sender = sender;
    }

    public static String getResetTemplateId() {
        return resetTemplateId;
    }
    @Value("${msgSms.resetTemplateId}")
    public void setResetTemplateId(String resetTemplateId) {
        SmsConstants.resetTemplateId = resetTemplateId;
    }

    public static String getRegisterTemplateId() {
        return registerTemplateId;
    }
    @Value("${msgSms.registerTemplateId}")
    public void setRegisterTemplateId(String registerTemplateId) {
        SmsConstants.registerTemplateId = registerTemplateId;
    }

    public static String getReceivingCodeTemplateId() {
        return receivingCodeTemplateId;
    }
    @Value("${msgSms.receivingCodeTemplateId}")
    public void setReceivingCodeTemplateId(String receivingCodeTemplateId) {
        SmsConstants.receivingCodeTemplateId = receivingCodeTemplateId;
    }

    public static String getLoginCodeTemplateId() {
        return loginCodeTemplateId;
    }
    @Value("${msgSms.loginCodeTemplateId}")
    public void setLoginCodeTemplateId(String loginCodeTemplateId) {
        SmsConstants.loginCodeTemplateId = loginCodeTemplateId;
    }

}