package com.knd.front.social.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_label")
@ApiModel(value="BaseLabelEntity对象", description="")
public class UserLabelEntity extends BaseEntity{

    @ApiModelProperty(value = "用户Id")
    private String userId;

    @ApiModelProperty(value = "关联标签id")
    private String labelId;

}
