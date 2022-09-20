package com.knd.manage.course.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author Lenovo
 */
@Data
public class TrainDetailRequest {
    @ApiModelProperty(value = "周训练日期")
    @NotNull(message = "周训练日期不能为空")
    private String weekDayName;

    @ApiModelProperty(value = "训练项目类型 0课程 1动作序列")
    @NotBlank(message = "训练项目类型不能为空")
    @Pattern(regexp = "^(0|1)$")
    private String itemType;

    @ApiModelProperty(value = "训练项目Id")
    @NotBlank(message = "训练项目Id不能为空")
    private String itemId;
}
