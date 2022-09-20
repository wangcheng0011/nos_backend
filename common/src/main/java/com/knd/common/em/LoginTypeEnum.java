package com.knd.common.em;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录方式枚举类
 * @author will
 */
@SuppressWarnings("ALL")
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    LOGIN_PASSWORD("1","手机号密码登录","loginPasswordHandler"),
    LOGIN_MOBILE("2","手机号一键登录","loginMobileHandler"),
    LOGIN_VERIFYCODE("3","手机号验证码登录","loginVerifyCodeHandler"),
    LOGIN_APPLE("4","apple登录","loginAppleHandler"),
    LOGIN_WX("5","微信登录","loginWxHandler"),
    LOGIN_QQ("6","QQ登录","loginQqHandler"),
    LOGIN_SMALLROUTINE("7","小程序登录","loginSmallRoutine")
    ;

    private final String code;
    private final String name;
    private final String className;

}
