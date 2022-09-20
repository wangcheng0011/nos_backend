package com.knd.front.live.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author will
 */
@Data
@TableName("lb_train_group")
@ApiModel
public class TrainGroupEntity extends BaseEntity {

    @ApiModelProperty(value = "房主ID")
    private String userId;
    @ApiModelProperty(value = "房间名称")
    private String groupName;
    @ApiModelProperty(value = "健身目标Id")
    private String targetId;
    @ApiModelProperty(value = "健身部位Id")
    private String partHobbyId;
    @ApiModelProperty(value = "是否公开")
    private String publicFlag;
    @ApiModelProperty(value = "简介")
    private String description;
    @ApiModelProperty(value = "人数")
    private Integer memberSize;
    @ApiModelProperty(value = "器材Id")
    private Integer equipmentId;

}
