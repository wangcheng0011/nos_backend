package com.knd.front.diagnosis.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/1
 * @Version 1.0
 */
@Data
public class RepairmanLoginRequest {
    @NotBlank(message = "账号不能为空")
    private String userName;
    @NotBlank(message = "密码不能为空")
    private String password;
}