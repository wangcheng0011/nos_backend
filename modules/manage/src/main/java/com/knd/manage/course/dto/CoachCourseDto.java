package com.knd.manage.course.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.manage.basedata.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zm
 * 教练课程列表
 */
@Data
public class CoachCourseDto {

    @ApiModelProperty(value = "课程主键id")
    private String id;

    @ApiModelProperty(value = "课程类别0课前咨询 1私教课程 2团课")
    private String courseType;

    @ApiModelProperty(value = "课程名称")
    private String courseName;

    @ApiModelProperty(value = "课程简介")
    private String courseSynopsis;

    @ApiModelProperty(value = "课程时间（分）")
    private String courseTime;

    @ApiModelProperty(value = "能量消耗（千卡）")
    private String consume;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "单价")
    private BigDecimal price;

    @ApiModelProperty(value = "预约人数")
    private String orderNum;

    @ApiModelProperty(value = "封面图片")
    private ImgDto picAttach;

    @ApiModelProperty(value = "难度id")
    private String difficultyId;
}
