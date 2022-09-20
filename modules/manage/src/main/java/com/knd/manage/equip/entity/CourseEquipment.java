package com.knd.manage.equip.entity;

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
 * @author wangcheng
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("course_equipment")
@ApiModel(value="CourseEquipment对象", description="")
public class CourseEquipment extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "设备编号")
    private String equipmentNo;

    @ApiModelProperty(value = "课程名称id")
    private String courseHeadId;

    @ApiModelProperty(value = "激活状态")
    private String status;

    @ApiModelProperty(value = "删除标志")
    private String deleted;

    @ApiModelProperty(value = "描述")
    private String remark;

}
