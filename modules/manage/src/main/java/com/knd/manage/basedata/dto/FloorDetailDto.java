package com.knd.manage.basedata.dto;

import com.knd.manage.basedata.entity.BaseElement;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
public class FloorDetailDto {

    private String id;

    @ApiModelProperty(value = "key值")
    private String keyValue;

    @ApiModelProperty(value = "版本")
    private String version;

    @ApiModelProperty(value = "显示顺序")
    private String sort;

    @ApiModelProperty(value = "楼层类型")
    private String floorType;

    @ApiModelProperty(value = "楼层名称")
    private String floorName;

    @ApiModelProperty(value = "楼层详情")
    private String floorDetail;

    @ApiModelProperty(value = "楼层备注")
    private String floorNote;

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

    @ApiModelProperty(value = "绑定的元素信息")
    private List<BaseElementDto> elementList;

    @ApiModelProperty(value = "后台展示图片")
    private ImgDto showUrl;

    @ApiModelProperty(value = "后台展示说明")
    private String description;
}
