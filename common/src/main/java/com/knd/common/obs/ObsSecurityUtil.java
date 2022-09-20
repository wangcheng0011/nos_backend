package com.knd.common.obs;

import com.knd.common.basic.JsonUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ObsSecurityUtil {
    private static final String SIGN_SEP = "\n";

    private static final String OBS_PREFIX = "x-obs-";

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final List<String> SUB_RESOURCES = Collections.unmodifiableList(Arrays.asList(
            "CDNNotifyConfiguration", "acl", "append", "attname", "backtosource", "cors", "customdomain", "delete",
            "deletebucket", "directcoldaccess", "encryption", "inventory", "length", "lifecycle", "location", "logging",
            "metadata", "modify", "name", "notification", "orchestration", "partNumber", "policy", "position", "quota",
            "rename", "replication", "requestPayment", "response-cache-control", "response-content-disposition",
            "response-content-encoding", "response-content-language", "response-content-type", "response-expires",
            "restore", "select", " storageClass", "storagePolicy", "storageinfo", "tagging", "torrent", "truncate",
            "uploadId", "uploads", "versionId", "versioning", "versions", "website", "x-image-process",
            "x-image-save-bucket", "x-image-save-object", "x-obs-security-token"));

    private String ak;

    private String sk;

    //获取格林时间
    public String formateDateForGMT(long time) {
        DateFormat serverDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        serverDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return serverDateFormat.format(time);
    }

    private String join(List<?> items, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            String item = items.get(i).toString();
            sb.append(item);
            if (i < items.size() - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public String urlEncode(String input) throws UnsupportedEncodingException {
        return URLEncoder.encode(input, DEFAULT_ENCODING)
                .replaceAll("%7E", "~")
                .replaceAll("%2F", "/");
    }

    private boolean isValid(String input) {
        return input != null && !input.equals("");
    }

    public String hamcSha1(String input) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        SecretKeySpec signingKey = new SecretKeySpec(this.sk.getBytes(DEFAULT_ENCODING), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        return Base64.getEncoder().encodeToString(mac.doFinal(input.getBytes(DEFAULT_ENCODING)));
    }


    //获取UTC时间
    public String formateDateForUTC(long time) {
        DateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        serverDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return serverDateFormat.format(time);
    }


    //拼接policy
    private String stringToSign(Map<String, String[]> conditions, String expiration) throws UnsupportedEncodingException {
        Map<String, Object> policy = new HashMap<>();
        policy.put("expiration", expiration);
        List<Object> list = new ArrayList<>();
        if (!conditions.isEmpty()) {
            String key;
            String[] value;
            for (Map.Entry<String, String[]> entry : conditions.entrySet()) {
                key = entry.getKey();
                value = entry.getValue();
                if (value.length > 1) {
                    //匹配
                    List<Object> la = new ArrayList<>();
                    for (String s : value) {
                        la.add(s);
                    }
                    list.add(la);
                } else {
                    //键值对
                    Map<String, String> m = new HashMap<>();
                    m.put(key, this.urlEncode(value[0]));
                    list.add(m);
                }
            }
        }
        policy.put("conditions", list);
        System.out.println("====================");
        System.out.println( JsonUtils.beanToJson(policy));
        System.out.println("====================");
        return  JsonUtils.beanToJson(policy);
    }

    /**
     * 获取加密后的安全策略描述
     */
    public String getPolicy( String sk,
                            Map<String, String[]> conditions, String expiration) throws Exception {
        this.sk = sk;
        return this.urlEncode(Base64.getEncoder().encodeToString(this.stringToSign(conditions, expiration).getBytes(DEFAULT_ENCODING)));
    }
    /**
     * 获取数字签名
     */
    public String getSignature(String stringToSign) throws Exception {
        //1. 获取stringToSign
//        String stringToSign = this.urlEncode(Base64.getEncoder().encodeToString(policy.getBytes(DEFAULT_ENCODING)));
        //2. 加密stringToSign，得到signature
        return this.urlEncode(Base64.getEncoder().encodeToString(this.hamcSha1(stringToSign).getBytes(DEFAULT_ENCODING)));
    }


}
