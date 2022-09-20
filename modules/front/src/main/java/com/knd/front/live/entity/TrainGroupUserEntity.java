package com.knd.front.live.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author will
 */
@Data
@TableName("lb_train_group_user")
@ApiModel
public class TrainGroupUserEntity{
    private static final Long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    protected String id;
    @ApiModelProperty(value = "会员ID")
    private String userId;
    @ApiModelProperty(value = "训练小组Id")
    private String trainGroupId;
    @ApiModelProperty(value = "是否管理员0-》否 1-》是")
    private String isAdmin;

    @ApiModelProperty(value = "加入时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime joinDate;

    @ApiModelProperty(value = "审核状态 0->审核中 1-》通过 ")
    private String joinStatus;


}
