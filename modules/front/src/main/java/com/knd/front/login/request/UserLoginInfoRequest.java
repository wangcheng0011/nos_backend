package com.knd.front.login.request;

import lombok.Data;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@Data
public class UserLoginInfoRequest {
    private String id;
    private String userId;
    private String loginInTime;
    private String equipmentNo;
    private String loginOutTime;
    private String deleted;
}