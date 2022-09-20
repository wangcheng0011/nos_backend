package com.knd.front.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/3
 * @Version 1.0
 */
@Data
public class UserTestActionDto {
    @ApiModelProperty(value = "动作id")
    private String actionId;
    @ApiModelProperty(value = "动作名称")
    private String actionName;
    @ApiModelProperty(value = "动作类型id")
    private String actionTypeId;

    @ApiModelProperty(value = "动作类型名称")
    private String actionTypeName;

    @ApiModelProperty(value = "是否双臂 0不添加 1单臂 2双臂")
    private String isTwoArms;

    @ApiModelProperty(value = "介绍视频")
    private String videoUrl;

    @ApiModelProperty(value = "封面图片")
    private String picUrl;

}