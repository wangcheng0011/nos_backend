package com.knd.front.login.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/9
 * @Version 1.0
 */
@Data
public class LogoutRequest {
    @ApiModelProperty(value = "用户id")
    private String userId;
}