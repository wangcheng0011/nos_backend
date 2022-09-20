package com.knd.front.social.request;

import com.knd.common.em.FollowOperationEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OperationFollowRequest {

    @ApiModelProperty(value = "用户id",required = true)
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty(value = "关注用户ID",required = true)
    @NotBlank(message = "关注用户ID不能为空")
    private String targetUserId;

    @ApiModelProperty(value = "关注操作",required = true)
    private FollowOperationEnum followOperation;
}
