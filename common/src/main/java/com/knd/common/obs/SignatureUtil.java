package com.knd.common.obs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignatureUtil {

    /**
     * 获取签名方法
     */
    public String getSignature(String ak, String sk, String bucketName, String objectName, long expireSeconds) throws Exception {
        this.ak = ak;
        this.sk = sk;
        Map<String, String[]> headers = new HashMap<>();
        headers.put("date", new String[]{expireSeconds + ""});
        Map<String, String> queries = new HashMap<>();
        return headerSignature("GET", headers, queries, bucketName, objectName);
    }
    //-///////////////////////////////////////////////////////////

    private static final String SIGN_SEP = "\n";

    private static final String OBS_PREFIX = "x-obs-";

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final List<String> SUB_RESOURCES = Collections.unmodifiableList(Arrays.asList(
            "CDNNotifyConfiguration", "acl", "append", "attname", "backtosource", "cors", "customdomain", "delete",
            "deletebucket", "directcoldaccess", "encryption", "inventory", "length", "lifecycle", "location", "logging",
            "metadata", "modify", "name", "notification", "orchestration", "partNumber", "policy", "position", "quota",
            "rename", "replication", "response-cache-control", "response-content-disposition",
            "response-content-encoding", "response-content-language", "response-content-type", "response-expires",
            "restore", "select", " storageClass", "storagePolicy", "storageinfo", "tagging", "torrent", "truncate",
            "uploadId", "uploads", "versionId", "versioning", "versions", "website", "x-image-process",
            "x-image-save-bucket", "x-image-save-object", "x-obs-security-token"));

    private String ak;

    private String sk;

    public String urlEncode(String input) throws UnsupportedEncodingException {
        return URLEncoder.encode(input, DEFAULT_ENCODING)
                .replaceAll("%7E", "~") //for browser
                .replaceAll("%2F", "/");
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

    private boolean isValid(String input) {
        return input != null && !input.equals("");
    }

    public String hamcSha1(String input) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        SecretKeySpec signingKey = new SecretKeySpec(this.sk.getBytes(DEFAULT_ENCODING), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        return Base64.getEncoder().encodeToString(mac.doFinal(input.getBytes(DEFAULT_ENCODING)));
    }

    private String stringToSign(String httpMethod, Map<String, String[]> headers, Map<String, String> queries,
                                String bucketName, String objectName) throws Exception {
        String contentMd5 = "";
        String contentType = "";
        String date = "";

        TreeMap<String, String> canonicalizedHeaders = new TreeMap<String, String>();

        String key;
        List<String> temp = new ArrayList<String>();
        for (Map.Entry<String, String[]> entry : headers.entrySet()) {
            key = entry.getKey();
            if (key == null || entry.getValue() == null || entry.getValue().length == 0) {
                continue;
            }

            key = key.trim().toLowerCase(Locale.ENGLISH);
            if (key.equals("content-md5")) {
                contentMd5 = entry.getValue()[0];
                continue;
            }

            if (key.equals("content-type")) {
                contentType = entry.getValue()[0];
                continue;
            }

            if (key.equals("date")) {
                date = entry.getValue()[0];
                continue;
            }

            if (key.startsWith(OBS_PREFIX)) {

                for (String value : entry.getValue()) {
                    if (value != null) {
                        temp.add(value.trim());
                    }
                }
                canonicalizedHeaders.put(key, this.join(temp, ","));
                temp.clear();
            }
        }

        if (canonicalizedHeaders.containsKey("x-obs-date")) {
            date = "";
        }


        // handle method/content-md5/content-type/date
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(httpMethod).append(SIGN_SEP)
                .append(contentMd5).append(SIGN_SEP)
                .append(contentType).append(SIGN_SEP)
                .append(date).append(SIGN_SEP);

        // handle canonicalizedHeaders
        for (Map.Entry<String, String> entry : canonicalizedHeaders.entrySet()) {
            stringToSign.append(entry.getKey()).append(":").append(entry.getValue()).append(SIGN_SEP);
        }

        // handle CanonicalizedResource
        stringToSign.append("/");
        if (this.isValid(bucketName)) {
            stringToSign.append(bucketName).append("/");
            if (this.isValid(objectName)) {
                stringToSign.append(this.urlEncode(objectName));
            }
        }

        TreeMap<String, String> canonicalizedResource = new TreeMap<String, String>();
        for (Map.Entry<String, String> entry : queries.entrySet()) {
            key = entry.getKey();
            if (key == null) {
                continue;
            }

            if (SUB_RESOURCES.contains(key)) {
                canonicalizedResource.put(key, entry.getValue());
            }
        }

        if (canonicalizedResource.size() > 0) {
            stringToSign.append("?");
            for (Map.Entry<String, String> entry : canonicalizedResource.entrySet()) {
                stringToSign.append(this.urlEncode(entry.getKey()));
                if (this.isValid(entry.getValue())) {
                    stringToSign.append("=").append(this.urlEncode(entry.getValue()));
                }
            }
        }
        return stringToSign.toString();
    }

    public String headerSignature(String httpMethod, Map<String, String[]> headers, Map<String, String> queries,
                                  String bucketName, String objectName) throws Exception {

        //1. stringToSign
        String stringToSign = this.stringToSign(httpMethod, headers, queries, bucketName, objectName);
        //加密 stringToSign 得到电子签名 并转成url格式
        return URLEncoder.encode(this.hamcSha1(stringToSign), "UTF-8");

    }
}
