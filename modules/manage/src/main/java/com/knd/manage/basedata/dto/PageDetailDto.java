package com.knd.manage.basedata.dto;

import com.knd.manage.basedata.entity.BaseFloor;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
public class PageDetailDto {

    String id;

    @ApiModelProperty(value = "key值")
    private String keyValue;

    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelProperty(value = "页面类型")
    private String pageType;

    @ApiModelProperty(value = "页面名称")
    private String pageName;

    @ApiModelProperty(value = "页面详情")
    private String pageDetail;

    @ApiModelProperty(value = "图片")
    private ImgDto imageUrl;

    @ApiModelProperty(value = "背景图片")
    private ImgDto backgroundUrl;

    @ApiModelProperty(value = "平台")
    private String platform;

    @ApiModelProperty(value = "跳转URL")
    private String skipUrl;

    @ApiModelProperty(value = "动态查询URL")
    private String searchUrl;

    @ApiModelProperty(value = "绑定的楼层信息")
    private List<BaseFloorSortDto> floorList;

    @ApiModelProperty(value = "后台展示图片")
    private ImgDto showUrl;

    @ApiModelProperty(value = "后台展示说明")
    private String description;
}
