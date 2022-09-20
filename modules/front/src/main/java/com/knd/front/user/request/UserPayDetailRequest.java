package com.knd.front.user.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserPayDetailRequest {

    @NotBlank
    @ApiModelProperty(value = "类型：0特色课程,1系列课程,2训练计划",required = true)
    private Integer type;

    @NotBlank
    @ApiModelProperty(value = "关联主键id",required = true)
    private String payId;
}
