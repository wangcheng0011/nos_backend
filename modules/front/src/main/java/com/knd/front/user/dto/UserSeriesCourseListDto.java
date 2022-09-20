package com.knd.front.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
public class UserSeriesCourseListDto {

    private String id;

    @ApiModelProperty(value = "系列课程名称")
    private String name;

    @ApiModelProperty(value = "简介")
    private String synopsis;

    @ApiModelProperty(value = "封面图片")
    private String picAttachUrl;
}
