package com.knd.manage.mall.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author zm
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@Data
public class CancelOrderCoachRequest {

    @ApiModelProperty(value = "课程时间id")
    @NotBlank
    @Size(max = 64)
    private String coachTimeId;

    @ApiModelProperty(value = "用户id")
    @Size(max = 64)
    @NotBlank
    private String userId;

}