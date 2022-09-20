package com.knd.front.user.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class UserPayCheckRequest {

    private List<UserPayDetailRequest> detailRequests;

    @NotBlank
    @ApiModelProperty(value = "购买人员Id",required = true)
    private String userId;
}
