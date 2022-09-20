package com.knd.auth.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "权限url集合", description = "")
public class AuthUrlDto {

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "路径")
    private String url;

    @ApiModelProperty(value = "访问方法")
    private String method;

}
