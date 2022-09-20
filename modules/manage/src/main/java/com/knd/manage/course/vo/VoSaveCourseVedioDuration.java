package com.knd.manage.course.vo;

import com.knd.common.userutil.UserUtils;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class VoSaveCourseVedioDuration {
    //userId从token获取
    private String userId ;
    @NotBlank
    @ApiParam("课程id")
    private String id;
    @NotBlank
    @ApiParam("视频总时长-分钟")
//    @Pattern(regexp = "^[0-9]([0-9]*)?")
    private String videoDurationMinutes;
    @NotBlank
    @ApiParam("视频总时长-秒")
//    @Pattern(regexp = "^([0-5])?[0-9]")
    private String videoDurationSeconds;
}
