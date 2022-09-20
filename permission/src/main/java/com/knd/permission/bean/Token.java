package com.knd.permission.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;


/**
 * Token Bean
 *
 * @author wcy
 */
@Data
public class Token implements Serializable {

    private static final Long serialVersionUID = 1L;

    private String userId;  // 用户ID

    private String userName;

    private String password;

    private String platform;

    private String unionid;

    private String qqOpenId;

    private String salt;

    private String token;

    //private Set<String> roleList;
    private String roleFlag;

    /**
     * 前端权限控制使用
     */
    private Set<String> permissionList;

    /**
     * 后台接口控制使用
     */
    private Set<AuthUrl> authUrlList;

    private String loginTime;

    private int expireSecond;

    private String nickName;

    private String mobile;
}
