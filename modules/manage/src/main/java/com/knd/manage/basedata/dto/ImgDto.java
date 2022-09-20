package com.knd.manage.basedata.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
