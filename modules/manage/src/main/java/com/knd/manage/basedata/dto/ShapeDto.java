package com.knd.manage.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
public class ShapeDto {

    private String id;

    @ApiModelProperty(value = "体型")
    private String shape;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "部位id")
    private String partId;

    @ApiModelProperty(value = "部位")
    private String part;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "选中图片")
    private ImgDto selectImg;

    @ApiModelProperty(value = "未选中图片")
    private ImgDto unSelectImg;

}
