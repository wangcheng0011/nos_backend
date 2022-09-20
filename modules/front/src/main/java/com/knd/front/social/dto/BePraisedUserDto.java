package com.knd.front.social.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 被赞列表信息
 */
@Data
public class BePraisedUserDto {

    @ApiModelProperty(value = "点赞者id")
    private String praiseUserId;

    @ApiModelProperty(value = "点赞者姓名")
    private String praiseUserName;

}
