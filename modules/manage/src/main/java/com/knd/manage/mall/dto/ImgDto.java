package com.knd.manage.mall.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "图片对象", description = "")
public class ImgDto {
    //封面图片url
    private String picAttachUrl;

    //封面图片原名称
    private String picAttachName;
    //封面图片新名称
    private String picAttachNewName;
    //封面图片大小
    private String picAttachSize;

}
