package com.knd.manage.equip.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class VoSaveReleaseStatus {
    private String userId;
    @NotBlank
    @Pattern(regexp = "^(0|1)$")
    @ApiParam("发布状态，0未发布，1已发布")
    private String releaseStatus;
    @NotBlank
    @ApiParam("版本信息id")
    @Size(max = 64)
    private String id;


}
