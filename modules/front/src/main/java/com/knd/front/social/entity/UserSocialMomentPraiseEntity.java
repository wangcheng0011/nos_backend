package com.knd.front.social.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_social_moment_praise")
@ApiModel(value="UserSocialMomentPraiseEntity对象", description="")
public class UserSocialMomentPraiseEntity extends BaseEntity {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "被点赞动态Id")
    private String momentId;

    @ApiModelProperty(value = "点赞状态 0点赞 1取消")
    private String praise;
}
