package com.knd.front.social.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@TableName("user_social_moment_comment")
@ApiModel(value="UserSocialMomentCommentEntity对象", description="")
public class UserSocialMomentCommentEntity {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "类型：0评论 1@")
    private String type;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "被@用户ID")
    private String callUserId;

    @ApiModelProperty(value = "动态id")
    private String momentId;

    @ApiModelProperty(value = "父Id")
    private String parentId;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "发表日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "删除标志")
    @TableField(fill = FieldFill.INSERT)
    private String deleted;
}
