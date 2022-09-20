package com.knd.manage.equip.vo;

import com.knd.common.userutil.UserUtils;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class VoSaveApkInfo {
    //userId从token获取
    private String userId;
    @NotBlank
    @Pattern(regexp = "^(KA|KS-YS|KS-TDX|NOS|KF|WJA|WJI|BT-H|BT-A|BT-MB)$")
    @ApiParam("版本号前缀")
    private String versionPrefix;
    @NotBlank
    @Size(max = 12)
    @ApiParam("版本号")
    private String appVersion;

    @NotBlank
    @Pattern(regexp = "^(0|1|2|3|4|5)$")
    @ApiParam("app类型")
    private String appType;

    @NotBlank
    @Pattern(regexp = "^(0|1)$")
    @ApiParam("是否强制更新")
    private String forceFlag;
    @NotBlank
    @Size(max = 256)
    @ApiParam("发布内容")
    private String content;
    @Pattern(regexp = "^(1|2)$")
    @ApiParam("操作类型")
    private String postType;
    @Size(max = 64)
    @ApiParam("版本信息id")
    private String id;

    //
    //
    @NotBlank
    @Size(max = 256)
    //apk附件原名称
    private String attachName;
    @NotBlank
    @Size(max = 256)
    //apk附件新名称
    private String attachNewName;
    @NotBlank
    @Size(max = 32)
    //apk附件大小
    private String attachSize;

}
