package com.knd.manage.course.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("course_body_part")
@ApiModel(value="CourseBodyPart对象", description="")
public class CourseBodyPart extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "课程id")
    private String courseId;

    @ApiModelProperty(value = "目标id")
    private String partId;


}
