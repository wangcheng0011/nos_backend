package com.knd.manage.course.dto;

import com.knd.manage.basedata.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zm
 * 教练列表
 */
@Data
public class CoachDto {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "擅长内容")
    private String content;

    @ApiModelProperty(value = "描述")
    private String depict;

    @ApiModelProperty(value = "简介")
    private String synopsis;

    @ApiModelProperty(value = "学员人数")
    private long traineeNum;

    @ApiModelProperty(value = "图片")
    private List<ImgDto> imageUrl;



}
