package com.knd.manage.course.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.manage.basedata.vo.VoUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zm
 */
@Data
public class VoSaveCoachCourse {
    private String userId;

    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;

    @Size(max = 64)
    private String courseId;

    @Size(max = 64)
    @ApiModelProperty(value = "教练id")
    private String coachId;

    @NotBlank
    @Pattern(regexp = "^(0|1|2)$")
    @ApiModelProperty(value = "课程类别0课前咨询 1私教课程 2团课")
    private String courseType;

    @NotBlank
    @Size(max = 500)
    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @NotBlank
    @Size(max = 500)
    @ApiModelProperty(value = "课程简介")
    private String courseSynopsis;

    @NotBlank
    @Size(max = 50)
    @ApiModelProperty(value = "能量消耗（千卡）")
    private String consume;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beginTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "图片")
    private VoUrl picAttachUrl;

    @NotBlank
    @ApiModelProperty(value = "难度id")
    private String difficultyId;

}
