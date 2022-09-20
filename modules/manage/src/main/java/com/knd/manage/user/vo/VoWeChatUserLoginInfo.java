package com.knd.manage.user.vo;

import lombok.Data;

@Data
public class VoWeChatUserLoginInfo {


    /**
     * 用户唯一的unionid
     */
    private String unionid;

    /**
     * 用户对应的应用的openid
     */
    private String openid;

//    /**
//     * 手机号,登录扫码是没有手机号的
//     */
//    private String phone;

    /**
     * 呢称
     */
    private String nickname;

    /**
     * 头像
     */
    private String headimgurl;

    /**
     *  性别 1-女 2-男
     */
    private String sex;

    /**
     * 使用语言
     */
    private String language;

    /**
     * 所属城市
     */
    private String city;

    /**
     * 所属省份
     */
    private String province;

    /**
     * 所属国家
     */
    private String country;

    /**
     * 权限
     */
    private String privilege;

}
