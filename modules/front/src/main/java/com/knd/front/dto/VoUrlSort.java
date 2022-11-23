package com.knd.front.dto;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author Lenovo
 */
@Data
public class VoUrlSort {
    //排序
    private String sort;
    //封面图片原名称
    @Size(max = 256, message = "最大长度为256")
    private String picAttachName;

    //封面图片新名称
    @Size(max = 256, message = "最大长度为256")
    private String picAttachNewName;

    //封面图片大小
    @Size(max = 32, message = "最大长度为32")
    private String picAttachSize;

}
