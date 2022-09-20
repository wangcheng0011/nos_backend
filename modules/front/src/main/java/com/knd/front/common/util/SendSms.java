package com.knd.front.common.util;

import com.knd.common.response.CustomResultException;
import com.knd.front.config.sms.SmsConstants;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/8/7
 * @Version 1.0
 */
@Slf4j
public class SendSms {
    //无需修改,用于格式化鉴权头域,给"X-WSSE"参数赋值
    private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";
    //无需修改,用于格式化鉴权头域,给"Authorization"参数赋值
    private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";


    public static String SendSmsCode(String type, String mobile) throws Exception {
        log.info("开始发送验证码,手机号是:{}",mobile);
        //验证码生成
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String templateParas = "[\"" + verifyCode + "\"]"; //模板变量，此处以单变量验证码短信为例，请客户自行生成6位验证码，并定义为字符串类型，以杜绝首位0丢失的问题（例如：002569变成了2569）。

        //请求Body,不携带签名名称时,signature请填null
        String body = buildRequestBody(SmsConstants.getSender(), mobile, type, templateParas, "",
                null);
        if (null == body || body.isEmpty()) {
            log.info("body is null");
            throw new CustomResultException("body is null");
        }

        //请求Headers中的X-WSSE参数值
        String wsseHeader = buildWsseHeader(SmsConstants.getAppKey(), SmsConstants.getAppSecret());
        if (null == wsseHeader || wsseHeader.isEmpty()) {
            log.info("wsse header is null.");
            throw new CustomResultException("wsse header is null.");
        }

        Writer out = null;
        BufferedReader in = null;
        StringBuffer result = new StringBuffer();
        HttpsURLConnection connection = null;
        InputStream is = null;

        //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
        HostnameVerifier hv = new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        trustAllHttpsCertificates();

        try {
            URL realUrl = new URL(SmsConstants.getUrl());
            connection = (HttpsURLConnection) realUrl.openConnection();

            connection.setHostnameVerifier(hv);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(true);
            //请求方法
            connection.setRequestMethod("POST");
            //请求Headers参数
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", AUTH_HEADER_VALUE);
            connection.setRequestProperty("X-WSSE", wsseHeader);

            connection.connect();
            out = new OutputStreamWriter(connection.getOutputStream());
            out.write(body); //发送请求Body参数
            out.flush();
            out.close();

            int status = connection.getResponseCode();
            if (200 == status) { //200
                is = connection.getInputStream();
            } else { //400/401
                is = connection.getErrorStream();
            }
            in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info(result.toString()); //打印响应消息实体
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (null != is) {
                    is.close();
                }
                if (null != in) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("验证码发送完成，验证码为: {}",verifyCode);
        }
        return verifyCode;
    }

    /**
     * 构造请求Body体
     *
     * @param sender
     * @param receiver
     * @param templateId
     * @param templateParas
     * @param statusCallBack
     * @param signature      | 签名名称,使用国内短信通用模板时填写
     * @return
     */
    static String buildRequestBody(String sender, String receiver, String templateId, String templateParas,
                                   String statusCallBack, String signature) {
        if (null == sender || null == receiver || null == templateId || sender.isEmpty() || receiver.isEmpty()
                || templateId.isEmpty()) {
            log.info("buildRequestBody(): sender, receiver or templateId is null.");
            throw new CustomResultException("buildRequestBody(): sender, receiver or templateId is null.");
        }
        Map<String, String> map = new HashMap<String, String>();

        map.put("from", sender);
        map.put("to", receiver);
        map.put("templateId", templateId);
        if (null != templateParas && !templateParas.isEmpty()) {
            map.put("templateParas", templateParas);
        }
        if (null != statusCallBack && !statusCallBack.isEmpty()) {
            map.put("statusCallback", statusCallBack);
        }
        if (null != signature && !signature.isEmpty()) {
            map.put("signature", signature);
        }

        StringBuilder sb = new StringBuilder();
        String temp = "";

        for (String s : map.keySet()) {
            try {
                temp = URLEncoder.encode(map.get(s), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            sb.append(s).append("=").append(temp).append("&");
        }

        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /**
     * 构造X-WSSE参数值
     *
     * @param appKey
     * @param appSecret
     * @return
     */
    static String buildWsseHeader(String appKey, String appSecret) {
        if (null == appKey || null == appSecret || appKey.isEmpty() || appSecret.isEmpty()) {
            log.info("buildWsseHeader(): appKey or appSecret is null.");
            throw new CustomResultException("buildWsseHeader(): appKey or appSecret is null.");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date()); //Created
        String nonce = UUID.randomUUID().toString().replace("-", ""); //Nonce

        MessageDigest md;
        byte[] passwordDigest = null;

        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update((nonce + time + appSecret).getBytes());
            passwordDigest = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        //如果JDK版本是1.8,请加载原生Base64类,并使用如下代码
        String passwordDigestBase64Str = Base64.getEncoder().encodeToString(passwordDigest); //PasswordDigest
        //如果JDK版本低于1.8,请加载三方库提供Base64类,并使用如下代码
        //String passwordDigestBase64Str = Base64.encodeBase64String(passwordDigest); //PasswordDigest
        //若passwordDigestBase64Str中包含换行符,请执行如下代码进行修正
        //passwordDigestBase64Str = passwordDigestBase64Str.replaceAll("[\\s*\t\n\r]", "");
        return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
    }

    /**
     * 忽略证书信任问题
     *
     * @throws Exception
     */
    static void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        return;
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        return;
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
        };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    public static String SendRecevicingSmsCode(String type, String mobile,String code) throws Exception {
        log.info("开始发送验证码,手机号是:{}",mobile);
        //验证码生成
        //String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String templateParas = "[\"" + code + "\"]"; //模板变量，此处以单变量验证码短信为例，请客户自行生成6位验证码，并定义为字符串类型，以杜绝首位0丢失的问题（例如：002569变成了2569）。

        //请求Body,不携带签名名称时,signature请填null
        String body = buildRequestBody(SmsConstants.getSender(), mobile, type, templateParas, "",
                null);
        if (null == body || body.isEmpty()) {
            log.info("body is null");
            throw new CustomResultException("body is null");
        }

        //请求Headers中的X-WSSE参数值
        String wsseHeader = buildWsseHeader(SmsConstants.getAppKey(), SmsConstants.getAppSecret());
        if (null == wsseHeader || wsseHeader.isEmpty()) {
            log.info("wsse header is null.");
            throw new CustomResultException("wsse header is null.");
        }

        Writer out = null;
        BufferedReader in = null;
        StringBuffer result = new StringBuffer();
        HttpsURLConnection connection = null;
        InputStream is = null;

        //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
        HostnameVerifier hv = new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        trustAllHttpsCertificates();

        try {
            URL realUrl = new URL(SmsConstants.getUrl());
            connection = (HttpsURLConnection) realUrl.openConnection();

            connection.setHostnameVerifier(hv);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(true);
            //请求方法
            connection.setRequestMethod("POST");
            //请求Headers参数
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Authorization", AUTH_HEADER_VALUE);
            connection.setRequestProperty("X-WSSE", wsseHeader);

            connection.connect();
            out = new OutputStreamWriter(connection.getOutputStream());
            out.write(body); //发送请求Body参数
            out.flush();
            out.close();

            int status = connection.getResponseCode();
            if (200 == status) { //200
                is = connection.getInputStream();
            } else { //400/401
                is = connection.getErrorStream();
            }
            in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info(result.toString()); //打印响应消息实体
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (null != is) {
                    is.close();
                }
                if (null != in) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("验证码发送完成，验证码为: {}",code);
        }
        return code;
    }
}