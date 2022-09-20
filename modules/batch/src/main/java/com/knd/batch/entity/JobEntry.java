package com.knd.batch.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel(value = "任务录入对象")
public class JobEntry {

    @NotNull(message = "类名不能为空")
    @ApiModelProperty(value = "任务名（类名）", name = "jobClassName", example = "com.knd.batch.job.AJob")
    private String jobClassName;

    @NotNull(message = "组名不能为空")
    @ApiModelProperty(value = "组名", name = "jobGroupName", example = "group1")
    private String jobGroupName;

    @ApiModelProperty(value = "cron表达式", name = "cronExpression", example = "0 0 12 * * ?")
    private String cronExpression;
}
