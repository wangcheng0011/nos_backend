package com.knd.manage.user.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class VoWxQQLogin {

    @NotBlank
    @Size(max = 64)
    @ApiParam("状态")
    private String state;

}
