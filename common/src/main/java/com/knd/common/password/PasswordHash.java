package com.knd.common.password;

import com.knd.common.uuid.UUIDUtil;
import org.apache.shiro.crypto.hash.SimpleHash;


/**
 * 密码加密配置
 * @author wcy
 *
 */
public class PasswordHash {
    
    /**
     * 加密方式
     */
	private String algorithmName;

	/**
	 * 加密次数
	 */
	private int hashIterations;

	/**
	 * 原密码
	 */
	private Object source;

	/**
	 * 盐值
	 */
	private String salt;

	/**
	 * 加密后的密文
	 */
	private String hexEncoded;

	/**
	 * 
	 * @param source
	 */
	public PasswordHash(Object source) {
	    this.algorithmName = "md5";
	    this.hashIterations = 996;
	    this.salt = UUIDUtil.getShortUUID();
	    this.source = source;
	    this.hexEncoded = new SimpleHash(algorithmName, source, salt, hashIterations).toHex();
	}
	
	/**
	 * 
	 * @param source
	 * @param salt
	 */
	public PasswordHash(Object source, String salt) {
	    this.algorithmName = "md5";
	    this.hashIterations = 996;
	    this.salt = salt;
	    this.source = source;
	    this.hexEncoded = new SimpleHash(algorithmName, source, salt, hashIterations).toHex();
	}

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public int getHashIterations() {
        return hashIterations;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHexEncoded() {
        return hexEncoded;
    }

    public void setHexEncoded(String hexEncoded) {
        this.hexEncoded = hexEncoded;
    }

    @Override
    public String toString() {
        return "PasswordHash [algorithmName=" + algorithmName + ", hashIterations=" + hashIterations + ", source="
                + source + ", salt=" + salt + ", hexEncoded=" + hexEncoded + "]";
    }
	
}
