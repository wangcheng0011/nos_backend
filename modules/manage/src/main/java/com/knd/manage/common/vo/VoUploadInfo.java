package com.knd.manage.common.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class VoUploadInfo {
    @NotBlank
    @Size(max = 256)
    @Pattern(regexp = "^(10|20|30)$")
    //10：视频，20：图片，30：apk
    private String folderType;
    @NotBlank
    @Size(max = 256)
    //文件新名字
    private String newName;

}
