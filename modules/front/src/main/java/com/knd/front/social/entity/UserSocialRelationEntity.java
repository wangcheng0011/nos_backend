package com.knd.front.social.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@TableName("user_social_relation")
@ApiModel(value="UserSocialRelationEntity对象", description="")
public class UserSocialRelationEntity {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "好友id")
    private String friendId;

    /**
     * 逻辑删除字段
     */
    @ApiModelProperty(value = "删除标志")
    @TableField(fill = FieldFill.INSERT)
    private String deleted;
}
