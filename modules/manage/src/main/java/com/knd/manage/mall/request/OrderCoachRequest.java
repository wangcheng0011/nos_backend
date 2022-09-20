package com.knd.manage.mall.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author zm
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@Data
public class OrderCoachRequest {

    @ApiModelProperty(value = "课程时间id")
    @NotBlank
    private List<String> coachTimeIdList;

    @ApiModelProperty(value = "用户id")
    @Size(max = 64)
    @NotBlank
    private String userId;

}