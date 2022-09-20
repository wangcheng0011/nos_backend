package com.knd.manage.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
public class HobbyDto {

    private String id;

    @ApiModelProperty(value = "爱好")
    private String hobby;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "部位id")
    private String partId;

    @ApiModelProperty(value = "部位")
    private String part;

    @ApiModelProperty(value = "选中图片")
    private ImgDto selectImg;

    @ApiModelProperty(value = "未选中图片")
    private ImgDto unSelectImg;

}
