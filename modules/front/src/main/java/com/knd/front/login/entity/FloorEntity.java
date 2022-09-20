package com.knd.front.login.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
public class FloorEntity {

    private String keyValue;

    @ApiModelProperty(value = "楼层类型")
    private String floorType;

    @ApiModelProperty(value = "楼层名称")
    private String floorName;

    @ApiModelProperty(value = "楼层详情")
    private String floorDetail;

    @ApiModelProperty(value = "楼层备注")
    private String floorNote;

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
