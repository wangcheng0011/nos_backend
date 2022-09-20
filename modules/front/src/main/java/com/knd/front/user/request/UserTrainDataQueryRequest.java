package com.knd.front.user.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


/**
 * @author will
 */
@Data
public class UserTrainDataQueryRequest {
    @ApiModelProperty(value = "用户id",required = true)
    @NotBlank(message = "")//用户id不能为空
    private String userId;
    @ApiModelProperty(value = "月份",example = "2020-05")
    //@Pattern(regexp = "((((19|20)\\d{2})-(0?(1|[3-9])|1[012])-(0?[1-9]|[12]\\d|30))|(((19|20)\\d{2})-(0?[13578]|1[02])-31)|(((19|20)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|((((19|20)([13579][26]|[2468][048]|0[48]))|(2000))-0?2-29))$",message = "日期格式必须为yyyy-MM-dd")
    private String month;
    @ApiModelProperty(value = "周开始日期",example = "2020-05-01")
    private String beginWeekDate;
    @ApiModelProperty(value = "周结束日期",example = "2020-05-07")
    private String endWeekDate;
    @ApiModelProperty(value = "指定日期",example = "2020-05-01")
    private String chooseDay;
}
