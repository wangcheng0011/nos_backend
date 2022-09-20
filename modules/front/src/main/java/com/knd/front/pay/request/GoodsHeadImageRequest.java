package com.knd.front.pay.request;

import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author will
 */
@Data
public class GoodsHeadImageRequest {
    @Size(max = 256, message = "最大长度为256")
    //封面图片原名称
    private String picAttachName;
    //    @NotBlank
    @Size(max = 256, message = "最大长度为256")
    //封面图片新名称
    private String picAttachNewName;
    //    @NotBlank
    @Size(max = 32, message = "最大长度为32")
    //封面图片大小
    private String picAttachSize;
}
