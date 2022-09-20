package com.knd.common.rsa;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RSAUtil {
    //私钥
    public static String privateKey  ="MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANErP2ArG8yAMOG1qOPqzePqCAsF/rkj1MmCAqLcpQNIiSLT2uwtSpsMNeGw1PxiLLTMg3A0BGIXgCVfdTqPblmfn9soza6c2ZKfznnhDtx0F1/3PP42h3nAyaexbU/KLVylfhdsIqyFkCESNj0lPSIpW/tMpcq3hgunC9QVykppAgMBAAECgYEAu0zLslTHzsaXfvXJZ1Xxyf3d9+kFYEnKmCq4K5gClz8TtcEZj1U7Mj2r3g4Xmmxa9gcQR5/8sM20bULeWXLobu/GSgxZvJV4aDKFCpDx7AShk5iB1rIzV6XcbOaDI42AEfnglSe65lo9HDNeX2kapfJB+oqML6tPODDyUMrlOoECQQDzMag52+Xm6CMZSmtw64a3FlCp8qitoA/x79nVMIInpuyzB/7woxeq8dmkq58YTn4HswRPGloIxlmhRaAsU51xAkEA3C7rrEAoK2AjdNoaE57tT9F2RwG9mvjqWIqQgnLcOm4LtgYhB/acLpcLYQ3cmD7YfgS1aheiSua2uFHiR6LgeQJBAO7+TNlXbg7G8L9I8F9PRtuklW7+ZqACXckgMg2dlzsKgzDJ2dN4I6k99eorMrU7ZgWA+uoV9ocLDkg72VXWcjECQEnkjT8PgOudpgiPMXUoPugsmY8W7HTX2H3CcuD0fyg0ykVaJTew8ZVJJ454wnFtleYk2mUybUrJxsRw5om4BbECQQC/BKwg6HZIGN+DQRyimQfIM9HO01eu1yINnrwRfTFPmjJrwTowOHcMuxr3kiLvNWmPWBYAe+sGu29o7fCCkLqU";

    /**
     * 生成公钥和私钥的方法
     * @param args
     */
    public static void main(String[] args) {
        Map<String, String> keyMap = genKeyPair();
        String content="新誉";
        System.out.println("公钥为:"+keyMap.get("publicKey"));
        String enResult=RSAEncrypt(content,keyMap.get("publicKey"));
        System.out.println(enResult);
        System.out.println("私钥为:"+keyMap.get("privateKey"));
        String deResult=RSADecrypt(enResult,keyMap.get("privateKey"));
        System.out.println(deResult);

    }


    public static  Map<String,String> genKeyPair(){
        try {
            KeyPairGenerator genKeyPair=KeyPairGenerator.getInstance("RSA");
            genKeyPair.initialize(1024,new SecureRandom());
            KeyPair keyPair = genKeyPair.generateKeyPair();
            Map<String,String> keyMap=new HashMap<>();
            PrivateKey privateKey = keyPair.getPrivate();
            keyMap.put("privateKey",Base64.getEncoder().encodeToString(privateKey.getEncoded()));
            PublicKey publicKey = keyPair.getPublic();
            keyMap.put("publicKey",Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            return keyMap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 使用公钥加密
     * @param content
     * @param publicKey
     * @return
     */
    public static String RSAEncrypt(String content,String publicKey){
        try {
            Cipher cipher=Cipher.getInstance("RSA");
            byte[] pub_bytes= Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec keySpec=new X509EncodedKeySpec(pub_bytes);
            KeyFactory keyFactory=KeyFactory.getInstance("RSA");
            PublicKey publicKey1=keyFactory.generatePublic(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE,publicKey1);
            byte[] bytes_result = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(bytes_result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String RSADecrypt(String content,String privateKey){
        try {
            byte[] pri_bytes=Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec keySpec=new PKCS8EncodedKeySpec(pri_bytes);
            KeyFactory keyFactory=KeyFactory.getInstance("RSA");
            PrivateKey privateKey1 = keyFactory.generatePrivate(keySpec);
            Cipher cipher=Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE,privateKey1);
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(content));
            return  new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
