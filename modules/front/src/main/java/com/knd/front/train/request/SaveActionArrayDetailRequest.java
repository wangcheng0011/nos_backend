package com.knd.front.train.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;

/**
 * @author will
 */
@Data
public class SaveActionArrayDetailRequest {
    @ApiModelProperty(value = "动作id")
    @NotBlank(message = "动作id不能为空")
    private String actionId;
    @ApiModelProperty(value = "计数目标时长")
    private String aimDuration;
    @ApiModelProperty(value = "计数目标次数")
    private String aimTimes;
    //@NotBlank(message = "排序")
    private String sort;
}