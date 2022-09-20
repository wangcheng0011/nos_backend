package com.knd.manage.website.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.manage.basedata.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
@Data
public class NewsDto {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "是否推荐")
    private String recommend;

    @ApiModelProperty(value = "分类")
    private String classify;

    @ApiModelProperty(value = "分类")
    private String classifyName;

    @ApiModelProperty(value = "图片")
    private ImgDto PicAttach;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "描述")
    private String represent;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "阅读数")
    private String readCount;

    @ApiModelProperty(value = "发布时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;


}
