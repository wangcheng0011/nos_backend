package com.knd.front.social.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OperationPraiseRequest {

    @ApiModelProperty(value = "用户id",required = true)
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty(value = "动态id",required = true)
    @NotBlank(message = "动态id不能为空")
    private String momentId;
}
