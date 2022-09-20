package com.knd.front.login.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BindingSecondaryListDto {

    private String id;

    @ApiModelProperty(value = "姓名")
    private String nickName;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "头像url")
    private String userHeadUrl;
}
