package com.knd.front.user.dto;

import com.knd.front.home.dto.BaseBodyPartDto;
import com.knd.front.home.dto.BaseTargetDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/3
 * @Version 1.0
 */
@Data
public class UserCourseInfoDto {
    @ApiModelProperty(value = "课程名称")
    private String course;

    @ApiModelProperty(value = "")
    private String trainingFlag;

    @ApiModelProperty(value = "课程id")
    private String courseId;

    @ApiModelProperty(value = "")
    private String trainCourseHeadInfoId;

    @ApiModelProperty(value = "")
    private String videoDuration;

    @ApiModelProperty(value = "")
    private String vedioBeginTime;

    @ApiModelProperty(value = "")
    private String vedioEndTime;

    @ApiModelProperty(value = "视频时长-分")
    private String videoDurationMinutes;

    @ApiModelProperty(value = "视频时长-秒")
    private String videoDurationSeconds;

    @ApiModelProperty(value = "课程图片url")
    private String picAttachUrl;

    @ApiModelProperty(value = "目标")
    private List<BaseTargetDto> targetList;

    @ApiModelProperty(value = "部位")
    private List<BaseBodyPartDto> partList;



}