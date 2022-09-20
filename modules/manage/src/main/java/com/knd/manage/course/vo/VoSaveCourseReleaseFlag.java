package com.knd.manage.course.vo;

import com.knd.common.userutil.UserUtils;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class VoSaveCourseReleaseFlag {
    //userId从token获取
    private String userId;
    @NotBlank
    @Size(max = 64)
    @ApiParam("课程id")
    private String id;
    @ApiParam("发布状态")
    @Pattern(regexp = "^(0|1)$")
    private String releaseFlag;
}
