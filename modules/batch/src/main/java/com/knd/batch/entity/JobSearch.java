package com.knd.batch.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel(value = "任务查询/删除")
public class JobSearch {

    @NotNull(message = "类名不能为空")
    @ApiModelProperty(value = "任务名（类名）", name = "jobClassName", example = "com.knd.batch.job.AJob")
    private String jobClassName;

    @NotNull(message = "组名不能为空")
    @ApiModelProperty(value = "组名", name = "jobGroupName", example = "group1")
    private String jobGroupName;

}
