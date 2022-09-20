package com.knd.front.user.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
public class SeriesCourseEntity {
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

    @ApiModelProperty(value = "封面图片id")
    private String filePath;

}
