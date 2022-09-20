package com.knd.manage.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
public class ElementDetailDto {

    private String id;

    @ApiModelProperty(value = "key值")
    private String keyValue;

    @ApiModelProperty(value = "显示顺序")
    private String sort;

    @ApiModelProperty(value = "元素名称")
    private String elementName;

    @ApiModelProperty(value = "元素详情")
    private String elementDetail;

    @ApiModelProperty(value = "元素备注")
    private String elementNote;

    @ApiModelProperty(value = "图片")
    private ImgDto imageUrl;

    @ApiModelProperty(value = "背景图片")
    private ImgDto backgroundUrl;

    @ApiModelProperty(value = "跳转URL")
    private String skipUrl;

    @ApiModelProperty(value = "动态查询URL")
    private String searchUrl;

    @ApiModelProperty(value = "后台展示图片")
    private ImgDto showUrl;

    @ApiModelProperty(value = "后台展示说明")
    private String description;
}
