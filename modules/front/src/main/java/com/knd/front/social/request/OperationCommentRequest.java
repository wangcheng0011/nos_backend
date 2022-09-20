package com.knd.front.social.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OperationCommentRequest {

    @ApiModelProperty(value = "类型：0评论 1@")
    @NotBlank(message = "类型不能为空")
    private String type;

    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty(value = "被@用户ID")
    private String callUserId;

    @ApiModelProperty(value = "动态id")
    @NotBlank(message = "动态id不能为空")
    private String momentId;

    @ApiModelProperty(value = "父Id")
    private String parentId;

    @ApiModelProperty(value = "内容")
    private String content;
}
