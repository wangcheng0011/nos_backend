package com.knd.front.login.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
public class PageDto {

    private String keyValue;

    @ApiModelProperty(value = "页面类型")
    private String pageType;

    @ApiModelProperty(value = "页面名称")
    private String pageName;

    @ApiModelProperty(value = "页面详情")
    private String pageDetail;

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

    @ApiModelProperty(value = "楼层信息")
    private List<FloorDto> floorDtoList;

}
