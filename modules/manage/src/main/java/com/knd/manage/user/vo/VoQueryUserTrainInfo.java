package com.knd.manage.user.vo;

import com.knd.common.userutil.UserUtils;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class VoQueryUserTrainInfo {
    @NotBlank
    @Size(max = 64)
    @ApiParam("会员id")
    private String userId ;
    @NotBlank
    @Size(max = 64)
    @ApiParam("训练id")
    private String trainReportId;
    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    @ApiParam("训练类型，1：课程训练，2：自由训练")
    private String trainType;

}
