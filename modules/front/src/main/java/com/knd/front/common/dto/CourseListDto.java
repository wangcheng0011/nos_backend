package com.knd.front.common.dto;

import com.knd.front.home.dto.BaseBodyPartDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zm
 */
@Data
public class CourseListDto {

    @ApiModelProperty(value = "课程id")
    private String courseId;

    @ApiModelProperty(value = "课程名称")
    private String course;

    @ApiModelProperty(value = "课程类型")
    private String courseType;

    @ApiModelProperty(value = "难度")
    private String difficulty;

    @ApiModelProperty(value = "封面图片")
    private String picAttachUrl;

    @ApiModelProperty(value = "视频介绍描述")
    private String remark;

    @ApiModelProperty(value = "视频时长")
    private String videoDurationMinutes;

    @ApiModelProperty(value = "锻炼部位")
    private List<BaseBodyPartDto> partList;
}