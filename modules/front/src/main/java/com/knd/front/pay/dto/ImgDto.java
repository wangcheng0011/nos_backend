package com.knd.front.pay.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "图片对象", description = "")
public class ImgDto {
    @ApiModelProperty(value = "图片id")
    private String picAttachId;

    @ApiModelProperty(value = "图片url")
    private String picAttachUrl;

    // 封面图片原名称
    @ApiModelProperty(value = "封面图片原名称")
    private String picAttachName;

    //封面图片新名称
    @ApiModelProperty(value = "封面图片新名称")
    private String picAttachNewName;
    //封面图片大小
    @ApiModelProperty(value = "图片大小")
    private String picAttachSize;

}
