package com.knd.front.live.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author will
 */
@Data
@TableName("lb_train_group_op")
@ApiModel()
public class TrainGroupOpEntity {

    private static final Long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    protected String id;

    @ApiModelProperty(value = "会员ID")
    private String userId;
    @ApiModelProperty(value = "房间Id")
    private String groupId;

    @ApiModelProperty(value = "状态")
    private String status;


    @ApiModelProperty(value = "作成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime createDate;

}
