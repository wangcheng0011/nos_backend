package com.knd.front.social.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 动态评论
 */
@Data
public class UserFansDto {

    @ApiModelProperty(value = "粉丝id")
    private String fansId;

    @ApiModelProperty(value = "粉丝姓名")
    private String fansName;

    @ApiModelProperty(value = "是否互关0否 1是")
    private String fansRelation;

    @ApiModelProperty(value = "头像url")
    private String headPicUrl;
}
