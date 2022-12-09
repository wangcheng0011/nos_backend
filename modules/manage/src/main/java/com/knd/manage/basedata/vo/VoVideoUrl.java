package com.knd.manage.basedata.vo;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class VoVideoUrl {
    //    @NotBlank
    @Size(max = 256, message = "最大长度为256")
    //介绍视频原名称
    private String videoAttachName;
    //    @NotBlank
    @Size(max = 256, message = "最大长度为256")
    //介绍视频新名称
    private String videoAttachNewName;
    //    @NotBlank
    @Size(max = 32, message = "最大长度为32")
    //介绍视频大小
    private String videoAttachSize;


}
