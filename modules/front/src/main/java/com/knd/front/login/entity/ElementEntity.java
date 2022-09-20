package com.knd.front.login.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
public class ElementEntity {

    private String keyValue;

    @ApiModelProperty(value = "显示顺序")
    private String sort;

    @ApiModelProperty(value = "元素名称")
    private String elementName;

    @ApiModelProperty(value = "元素详情")
    private String elementDetail;

    @ApiModelProperty(value = "元素备注")
    private String elementNote;

    @ApiModelProperty(value = "图片Id")
    private String imageUrlId;

    @ApiModelProperty(value = "图片")
    private String imageUrl;

    @ApiModelProperty(value = "背景图片")
    private String backgroundUrl;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "跳转URL")
    private String skipUrl;

    @ApiModelProperty(value = "动态查询URL")
    private String searchUrl;
}
