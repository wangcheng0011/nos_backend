package com.knd.manage.user.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class VoSaveFrozenFlag {
    @NotBlank
    @Pattern(regexp = "^(0|1)$")
    @ApiParam("冻结状态，0：正常，1：冻结")
    private String frozenFlag;
    @NotBlank
    @ApiParam("会员id")
    @Size(max = 64)
    private String userId;

}
