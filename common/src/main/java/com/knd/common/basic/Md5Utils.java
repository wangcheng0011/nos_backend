package com.knd.common.basic;

import com.alibaba.fastjson.JSONObject;
import com.knd.common.utils.HttpUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Md5Utils {
    // 获取md5加密算法对象
    private final static MessageDigest MD;
    private final static char[] CS = { '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    static {
        try {
            MD = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String md5(String text) {
        try {
            byte[] bs = MD.digest(text.getBytes("utf-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bs) {
                sb.append(CS[(b >> 4) & 0x0f]);
                sb.append(CS[b & 0x0f]);
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        System.out.println(Md5Utils.md5("sdsdsd"));

        String url = "http://my.wlwx.com:6004/req/api/server/Onekey/mobileQuery";
        String master_secret = "220F6E9370AC809D3C7C804E4D93E63E";
        String paramStr = "";
        Map<String,String> paramMap = new HashMap<>();
        String timeStamp = System.currentTimeMillis()+"";
        String signKey = Md5Utils.md5(master_secret+timeStamp);
        paramMap.put("sign",signKey);
        paramMap.put("app_id","F5734D5F2AFAC206AF65CA074E499E3F");
        paramMap.put("time_stamp",timeStamp);
        paramMap.put("access_token","F5734D5F2AFAC206AF65CA074E499E3F");
        paramStr = JSONObject.toJSONString(paramMap);

        String s = null;
        try {
            s = HttpUtils.httpPost(url, paramStr);
            JSONObject jsonResult = JSONObject.parseObject(s);
            System.out.println(jsonResult);
            String phone = jsonResult.getJSONObject("object").getString("tel");
        } catch (Exception e) {

            e.printStackTrace();
        }

    }
}
