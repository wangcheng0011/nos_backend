package com.knd.front.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
public class UserSeriesCourseDto {

    @ApiModelProperty(value = "系列课程名称")
    private String name;

    @ApiModelProperty(value = "简介")
    private String synopsis;

    @ApiModelProperty(value = "详情介绍")
    private String introduce;

    @ApiModelProperty(value = "难度")
    private String difficulty;

    @ApiModelProperty(value = "消耗")
    private String consume;

    @ApiModelProperty(value = "封面图片")
    private String picAttachUrl;

    @ApiModelProperty(value = "长图")
    private List<String> attachUrl;

    @ApiModelProperty(value = "分类数量")
    private List<CourseTypeNumDto> courseTypeNumList;

    @ApiModelProperty(value = "课程")
    private List<UserCoursePayDto> coursePayDtoList;

}
