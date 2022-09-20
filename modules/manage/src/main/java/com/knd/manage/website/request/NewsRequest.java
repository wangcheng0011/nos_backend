package com.knd.manage.website.request;

import com.knd.manage.basedata.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangcheng
 */
@Data
public class NewsRequest {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "是否推荐 0是 1否")
    private String recommend;

    @ApiModelProperty(value = "分类")
    private String classify;

    @ApiModelProperty(value = "图片")
    private ImgDto picAttach;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "描述")
    private String represent;

    @ApiModelProperty(value = "内容")
    private String content;

 /*   @ApiModelProperty(value = "发布时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;*/




}