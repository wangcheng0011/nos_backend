package com.knd.manage.user.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class VoDeleteUser {
    private String userId;
    @NotBlank
    @ApiParam("会员id")
    @Size(max = 64)
    private String uid;

}
