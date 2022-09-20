package com.knd.manage.user.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.knd.common.utils.HttpUtils;
import com.knd.manage.user.vo.VoWeChatUserLoginInfo;
import lombok.extern.log4j.Log4j2;

import java.net.URLEncoder;

@Log4j2
public class WeChatUtil {



    /***
     * 获取网页版微信扫码url地址
     * @param wxAppId 微信appid
     * @param checkStateCore 扫码状态检查码
     * @param callBackMethod 回调方法
     */
    public static String getWeChatUrl(String wxAppId,String checkStateCore,String callBackMethod){
        log.info("微信扫码后回调方法====="+callBackMethod);
        return  "https://open.weixin.qq.com/connect/qrconnect?appid="+wxAppId
                +"&redirect_uri="+ URLEncoder.encode(callBackMethod)
                +"&response_type=code"
                +"&scope=snsapi_login"
                +"&state="+checkStateCore
                +"#wechat_redirect";
    }


    /**
     * 获取微信用户的基本信息
     * @param wxAppId 微信appid
     * @param wxAppSecret 微信的secret
     * @param code 对应扫码得到的code
     */
    public static VoWeChatUserLoginInfo getWeChatUserInfo(String wxAppId, String wxAppSecret, String code) throws Exception {
        //获取用户openid
        String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+wxAppId
                + "&secret="+wxAppSecret
                + "&code="+code
                + "&grant_type=authorization_code";
        String jsonString= HttpUtils.httpGet(url);
        JSONObject jsonObject = JSONObject.parseObject(jsonString) ;
        String openid = jsonObject.getString("openid");
        String accessToken = jsonObject.getString("access_token");
        //微信公开的用户信息
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken
                +"&openid="+openid
                +"&lang=zh_CN";
        String userInfoString=HttpUtils.httpGet(infoUrl);
        log.info("回调后获取微信用户信息: "+userInfoString);
        return JSON.toJavaObject(JSONObject.parseObject(userInfoString),VoWeChatUserLoginInfo.class);
    }

}
