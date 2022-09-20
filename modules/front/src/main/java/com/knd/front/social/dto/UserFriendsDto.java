package com.knd.front.social.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 动态评论
 */
@Data
public class UserFriendsDto {

    @ApiModelProperty(value = "好友id")
    private String friendId;

    @ApiModelProperty(value = "好友姓名")
    private String friendName;

    @ApiModelProperty(value = "头像url")
    private String headPicUrl;
}
