package com.knd.front.user.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;


/**
 * @author will
 */
@Data
public class UserActionPowerTestRequest{
    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;
    @ApiModelProperty(value = "动作类型id")
    @NotBlank(message = "动作类型id不能为空")
    private String actionType;
    @ApiModelProperty(value = "动作类型名称")
    @NotBlank(message = "动作类型名称不能为空")
    private String actionTypeName;
    @ApiModelProperty(value = "动作id")
    @NotBlank(message = "动作id不能为空")
    private String actionId;
    @ApiModelProperty(value = "动作名称")
    @NotBlank(message = "动作名称不能为空")
    private String actionName;
    @ApiModelProperty(value = "最大测试力量",example = "20")
    @NotBlank(message = "最大测试力量不能为空")
    private String testPower;

    @ApiModelProperty(value = "平均测试力量",example = "20")
    @NotBlank(message = "平均测试力量不能为空")
    private String avgPower;

    @ApiModelProperty(value = "力量等级",example = "20")
    @NotBlank(message = "力量等级不能为空")
    private String powerLevel;

}
