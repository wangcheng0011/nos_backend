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
@TableName("power_test")
@ApiModel(value="PowerTestEntity对象", description="")
public class PowerTestEntity extends BaseEntity {
    private static final long serialVersionUID=1L;
    @ApiModelProperty(value = "性别 0男1女")
    private String gender;

    @ApiModelProperty(value = "难度ID")
    private String difficultyId;

    @ApiModelProperty(value = "总测试时长(秒)")
    private String totalDuration;

    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "图标id")
    private String picAttachId;
}
