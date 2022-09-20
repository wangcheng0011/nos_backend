package com.knd.front.entity;

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
 * @author llx
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_course_type")
@ApiModel(value="BaseCourseType对象", description="")
public class BaseCourseType extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "课程分类名称")
    private String type;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "是否大屏显示")
    private String appHomeFlag;

    @ApiModelProperty(value = "大屏显示排序号")
    private String sort;


}
