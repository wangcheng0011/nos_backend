package com.knd.front.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.front.pay.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author will
 */
@Data
public class TrainProgramDto {

    private String id;

    @ApiModelProperty(value = "图片url")
    private String picAttachUrl;

    @ApiModelProperty(value = "计划名称")
    private String programName;

    /**
     * 会员Id
     */
    private String userId;
    /**
     * 计划开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    //@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime beginTime;
    /**
     * 计划结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    //@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime endTime;
    /**
     * 计划训练天数
     */
    private String trainTotalDayNum;
    /**
     * 循环周数
     */
    private String trainWeekNum;
    /**
     * 每周训练天数
     */
    private String trainWeekDayNum;

    /**
     * 来源
     */
    private String source;

    /**
     * 计划类型
     */
    private String type;


    @ApiModelProperty(value = "图片url")
    private ImgDto picAttachUrlImgDto;
}
