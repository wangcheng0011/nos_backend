package com.knd.front.train.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author will
 */
@Data
public class TrainProgramDayItemRequest {

    @ApiModelProperty(value = "训练项目类型 0课程 1动作序列")
    @NotBlank(message = "训练项目类型不能为空")
    @Pattern(regexp = "^(0|1)$")
    private String itemType;
    @ApiModelProperty(value = "训练项目Id")
    @NotBlank(message = "训练项目Id不能为空")
    private String itemId;


}
