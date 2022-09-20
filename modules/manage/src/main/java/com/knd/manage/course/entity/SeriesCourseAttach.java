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
@TableName("series_course_attach")
@ApiModel(value="SeriesCourseAttach对象", description="")
public class SeriesCourseAttach extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "系列课程id")
    private String seriesId;

    @ApiModelProperty(value = "图片id")
    private String attachId;

}
