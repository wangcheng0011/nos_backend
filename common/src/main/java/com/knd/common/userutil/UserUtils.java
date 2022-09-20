package com.knd.common.userutil;

import com.knd.common.basic.StringUtils;
import com.knd.common.rsa.RSAUtil;
import com.knd.common.uuid.UUIDUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.*;
import java.util.*;

public class UserUtils {


    public static Map<String, String> genKeyPair() {
        try {
            KeyPairGenerator genKeyPair = KeyPairGenerator.getInstance("RSA");
            genKeyPair.initialize(1024, new SecureRandom());
            KeyPair keyPair = genKeyPair.generateKeyPair();
            Map<String, String> keyMap = new HashMap<>();
            PrivateKey privateKey = keyPair.getPrivate();
            keyMap.put("privateKey", Base64.getEncoder().encodeToString(privateKey.getEncoded()));
            PublicKey publicKey = keyPair.getPublic();
            keyMap.put("publicKey", Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            return keyMap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Request
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest();
    }


    /**
     * 获取请求tokenCode
     *
     * @return String
     */
    public static String getTokenCode() {
        return getRequest().getHeader(HttpHeaders.AUTHORIZATION);
    }


    /**
     * 获取token
     *
     * @return
     */
    public static Claims getToken() {
        String token = getTokenCode();
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        try {
            return Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(RSAUtil.privateKey))
                    .parseClaimsJws(token).getBody();
        } catch (Exception ex) {
            return null;
        }
    }


    //获得userId
    public static String getUserId() {
        return (String) getToken().get("userId");
        //未使用token权限，先使用默认的uuid作为测试
//        return UUIDUtil.getShortUUID();
    }

    //获得platform
    public static String getPlatform() {
        return (String) getToken().get("platform");
    }

    /**
     * @param userId   用户id
     * @param platform 平台
     * @return
     */
    public static String createToken(String userId, String platform) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(RSAUtil.privateKey);

        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder()
//                .setHeaderParam("type", "JWT")
//                .setSubject(userId.toString())
                .claim("userId", userId) // 设置载荷信息
                .claim("platform", platform)
                .setIssuedAt(new Date())
                .signWith(signatureAlgorithm, signingKey);

        //生成JWT
        return builder.compact();

    }
}
