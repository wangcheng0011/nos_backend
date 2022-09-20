package com.knd.manage.course.vo;

import com.knd.common.userutil.UserUtils;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class VoSaveCourseBlockInfo {
    //userId从token获取
    private String userId ;
    @NotBlank
    @ApiParam("课程id")
    private String id;
    @NotBlank
    @ApiParam("block名称")
    private String block;
    @NotBlank
    @ApiParam("目标组数")
    private String aimSetNum;
    @NotBlank
    @ApiParam("排序号")
    private String sort;

    @NotBlank
    @Size(max = 1)
    @Pattern(regexp = "^(1|2)$")
    @ApiParam("操作类型")
    private String postType;

    @ApiParam("block id")
    private String blockId;

}
