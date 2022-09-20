package com.knd.front.social.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_label")
@ApiModel(value="BaseLabelEntity对象", description="")
public class BaseLabelEntity extends BaseEntity {

    @ApiModelProperty(value = "标签类别")
    private String type;

    @ApiModelProperty(value = "标签名称")
    private String label;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "图片id")
    private String imageUrlId;

}
