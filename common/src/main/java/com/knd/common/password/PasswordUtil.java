
package com.knd.common.password;

import com.knd.common.basic.StringUtils;
import com.knd.common.uuid.UUIDUtil;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;


/**
 * 密码辅助工具类
 * @author wcy
 *
 */
public class PasswordUtil extends DigestUtils {
    
    /**
     * 
     * @param data
     * @return
     */
    public static String md5Hex(final String data) {
        return DigestUtils.md5DigestAsHex(data.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * 
     * @param bytes
     * @return
     */
    public static String md5Hex(final byte[] bytes) {
        return DigestUtils.md5DigestAsHex(bytes);
    }
    
    /**
     * 盐值加密
     * @param source
     * @return
     */
    public static PasswordHash encrypt(Object source) {
        return new PasswordHash(source);
    }
    
    /**
     * 盐值加密
     * @param source
     * @return
     */
    public static PasswordHash encrypt(Object source, String salt) {
        return new PasswordHash(source, salt);
    }
    
    /**
     * 盐值加密
     * @param source
     * @return
     */
    public static String encryptString(Object source, String salt) {
        return new PasswordHash(source, salt).getHexEncoded();
    }
    
    /**
     * 密码校验
     * @param source
     * @param salt
     * @param password
     * @return
     */
    public static boolean verify(Object source, String salt, String password) {
        return password.equals(encryptString(source, salt));

    }

    /**
     * 登录用户密码校验
     * @param password
     * @param salt
     * @param userPassWord
     * @return
     */
    public static boolean checkPassword(String password,String salt,String userPassWord) {
        if(StringUtils.isAllNotEmptyObjects(password, salt, userPassWord)) {
            return verify(password, salt, userPassWord);
        }
        return false;
    }

    /**
     * 获取默认密码
     * @return
     */
    public static String getDefaultPassword() {
        return UUIDUtil.getShortUUID();
    }
    
    public static void main(String[] args) {
        
        
    }
    

}
