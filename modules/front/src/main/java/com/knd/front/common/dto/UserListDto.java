package com.knd.front.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.common.em.FollowStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author zm
 */
@Data
@ApiModel(description = "用户列表")
public class UserListDto {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "头像")
    private String headPicUrl;

    @ApiModelProperty(value = "粉丝数量")
    private String fansNum;

    @ApiModelProperty(value = "动态数量")
    private String momentNum;

    @ApiModelProperty(value = "是否是教练0否 1是")
    private String isCoach;

    @ApiModelProperty(value = "关注状态")
    private FollowStatusEnum followStatus;

}
