package com.knd.front.user.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


/**
 * @author will
 */
@Data
public class UserTrainInfoQueryRequest {
    @ApiModelProperty(value = "用户id",required = true)
    @NotBlank(message = "")//用户id不能为空
    private String userId;
    @ApiModelProperty(value = "运动类型必须是1（课程）2（动作）3（动作序列）4（自由训练）",required = true)
    @NotBlank(message = "运动类型必须是1（课程）2（动作）3（动作序列）4（自由训练）")
    private String trainType;
    @ApiModelProperty(value = "当前页码",required = true)
    @NotBlank(message = "当前页码不能为空")
    private String currentPage;
    @ApiModelProperty(value = "月份",example = "2020-05")
    private String month;
    @ApiModelProperty(value = "周开始日期",example = "2020-05-01")
    private String beginWeekDate;
    @ApiModelProperty(value = "周结束日期",example = "2020-05-07")
    private String endWeekDate;
    @ApiModelProperty(value = "指定日期",example = "2020-05-01")
    private String chooseDay;
}
