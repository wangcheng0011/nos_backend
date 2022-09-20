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
 * @since 2020-07-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("course_trainning_block")
@ApiModel(value="CourseTrainningBlock对象", description="")
public class CourseTrainningBlock extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "课程id")
    private String courseId;

    @ApiModelProperty(value = "block名称")
    private String block;

    @ApiModelProperty(value = "目标组数")
    private String aimSetNum;

    @ApiModelProperty(value = "排序号")
    private String sort;


}
