package com.knd.front.social.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 动态评论
 */
@Data
public class UserFollowDto {

    @ApiModelProperty(value = "关注人id")
    private String followId;

    @ApiModelProperty(value = "关注人姓名")
    private String followName;

    @ApiModelProperty(value = "是否互关0否 1是")
    private String followRelation;

    @ApiModelProperty(value = "头像url")
    private String headPicUrl;
}
