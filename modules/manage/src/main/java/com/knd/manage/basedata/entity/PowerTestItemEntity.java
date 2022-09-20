package com.knd.manage.basedata.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author zm
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("power_test_item")
@ApiModel(value="PowerTestItemEntity对象", description="")
public class PowerTestItemEntity extends BaseEntity {
    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "测试项ID")
    private String powerTestId;

    @ApiModelProperty(value = "动作ID")
    private String actionId;

    @ApiModelProperty(value = "部位ID")
    private String bodyPartId;

    @ApiModelProperty(value = "动作名称")
    private String actionName;

    @ApiModelProperty(value = "是否使用器材 0否 1是")
    private String useEquipmentFlag;

    @ApiModelProperty(value = "力量值")
    private String power;

    @ApiModelProperty(value = "标准值")
    private String kpa;

    @ApiModelProperty(value = "测试时长(秒)")
    private String duration;

    @ApiModelProperty(value = "排序")
    private String sort;
}
