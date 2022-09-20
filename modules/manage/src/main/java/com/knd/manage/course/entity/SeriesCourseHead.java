package com.knd.manage.course.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zm
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("series_course_head")
@ApiModel(value="SeriesCourseHead对象", description="")
public class SeriesCourseHead extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "系列课程名称")
    private String name;

    @ApiModelProperty(value = "简介")
    private String synopsis;

    @ApiModelProperty(value = "详情介绍")
    private String introduce;

    @ApiModelProperty(value = "难度")
    private String difficulty;

    @ApiModelProperty(value = "消耗")
    private String consume;

    @ApiModelProperty(value = "封面图片id")
    private String picAttachId;

}
