package com.knd.common.rsa;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lake on 17-4-12.
 */
public class RSACoder {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCndk0Lc2lDeOxv6QXSWMr3vpYRkddxnRDMCgspzFf6h24maSdnhcOIplXuVjHx2Hof3+awarpvchvxVR5YZszLZhCzPoWSrtJak8AeagwN0sd39t5ReeVvgKcLzvlEC7c28j0BTDy2Ja69zFor03K+Q8kZ1AbX3Bzg5lny/3ukgQIDAQAB";
    private static String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAILHAoVIQs2Tv5qhmWnQTcRiaH9R4kpVvaLGODX29fHlkUwDywk4mX9pQ8aEwAkq7GwB2hFLDNch1I1f4hV23kn98Wa7AvSfciDG3plTBGPekRSAxExkiI7Vk90/Y+nIZ9U4yWs6KnjMc/qFYlJ8VGW7txHjivFFtEIirrDFmUWpAgMBAAECgYBSuO2oPzg6b54Agv3Wx/OZKKJu/u3EFY4522aNBCU4mP0adz/EGnBF/uDnPut2IRehjl5RGAcMQhMARkltSF58yXkV5ZNLTifLtDwPpgEDhflS9nsttB7MJkLD5PUgfhy/RA91uQSC1GiP0LDAl0U0mbjBFR7WWdY0gP78wIes/QJBAME64FtePrWIN+/Zh6vZtKcg6Y+F9W5kVyIjMHOwfZ2qW0dXg/gFq6Pt/GFRZ+voiS5DNqIRN089ysg5chqiM7MCQQCtQolrj4k2r2+RW11DiqPo1scxWveyyJqyrMdmonZRWm7zwZd4OejmB+DAMDSDmj7b1Zhjvj0/OUW6EXb3KqMzAkEAjSw/yGFFvYklHQY3uhVXiBdirKo5UwraIiXFe3CBhhR1ldzZnPY/nYf9MPixzUhaGUOgAa5EU4+YvMpDlDPXQwJATUPA2LC39iUiUeY2nyoykmFFj2ML1zTSmB+pBMIqlXkIxbdtfvYEQKD+v3Y/1PhyU8LbJI/v7EP0Llgff3YKNQJAMw2NaU3NgX89j2NpfsV9L/tB5Wi7WTa2JXPsjhTphzw3I7J6OSkmvt1JbQq21iqz8DlzULatkiuVv8VN3pooIw==";


    public static byte[] decryptBASE64(String key) {
        return Base64.decodeBase64(key);
    }

    public static String encryptBASE64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = decryptBASE64(privateKey);
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }

    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }


    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(String data, String key)
            throws Exception {
        return decryptByPrivateKey(decryptBASE64(data), key);
    }

    /**
     * 解密<br>
     * 用公钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(String data, String key)
            throws Exception {
        // 对公钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data.getBytes());
    }


    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String key)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBASE64(key);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Key> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Key> keyMap)
            throws Exception {
        Key key = keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Key> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        Map<String, Key> keyMap = new HashMap(2);
        keyMap.put(PUBLIC_KEY, keyPair.getPublic());// 公钥
        keyMap.put(PRIVATE_KEY, keyPair.getPrivate());// 私钥
        System.out.println("PUBLIC_KEY:" + getPublicKey(keyMap));
        System.out.println("PRIVATE_KEY:" + getPrivateKey(keyMap));
        return keyMap;
    }

    public static String encrypt(String data)
            throws Exception {
        byte[] encodedData = RSACoder.encryptByPublicKey(data, PUBLIC_KEY);
        return encryptBASE64(encodedData);
    }

    public static String decrypt(String data) throws Exception {
        byte[] bytes = decryptBASE64(data);
        byte[] decodedData = RSACoder.decryptByPrivateKey(bytes,
                PRIVATE_KEY);
        return new String(decodedData);
    }

    public static void main(String[] args) throws Exception {
//        String encrypt = encrypt("123456");
//        System.out.println(encrypt);
//        String decrypt = decrypt(encrypt);
//        System.out.println(decrypt);
        initKey();
//        System.err.println("公钥加密——私钥解密");
//        String inputStr = "123456";
//        byte[] encodedData = RSACoder.encryptByPublicKey(inputStr, PUBLIC_KEY);
//        String e=encryptBASE64(encodedData);
//        System.out.println(e);
//        byte[] bytes = decryptBASE64(e);
//        byte[] decodedData = RSACoder.decryptByPrivateKey(bytes,
//                PRIVATE_KEY);
//        String outputStr = new String(decodedData);
//        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
    }
}