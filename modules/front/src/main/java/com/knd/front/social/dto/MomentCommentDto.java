package com.knd.front.social.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.common.em.FollowStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 动态评论
 */
@Data
public class MomentCommentDto {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "动态id")
    private String momentId;

    @ApiModelProperty(value = "操作类型0回复 1@")
    private String type;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户头像")
    private String userHeadUrl;

    @ApiModelProperty(value = "被@用户ID")
    private String callUserId;

    @ApiModelProperty(value = "被@用户姓名")
    private String callUserName;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "评论时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "子回复信息")
    private List<MomentCommentDto> sunList;
}
