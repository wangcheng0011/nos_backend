package com.knd.front.social.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("user_social_moment_attach")
@ApiModel(value="UserSocialMomentAttachEntity对象", description="")
public class UserSocialMomentAttachEntity {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "动态Id")
    private String momentId;

    @ApiModelProperty(value = "照片id")
    private String attachId;

    /**
     * 逻辑删除字段
     */
    @ApiModelProperty(value = "删除标志")
    @TableField(fill = FieldFill.INSERT)
    private String deleted;
}
