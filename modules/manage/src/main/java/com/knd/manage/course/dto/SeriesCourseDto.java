package com.knd.manage.course.dto;

import com.knd.manage.basedata.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
public class SeriesCourseDto {

    private String id;

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
    private ImgDto picAttach;

    private List<ImgDto> imageUrl;

    private List<CourseDto> courseList;
}
