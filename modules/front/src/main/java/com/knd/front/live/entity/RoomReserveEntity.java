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
@TableName("lb_room_reserve")
@ApiModel
public class RoomReserveEntity {

    private static final Long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    protected String id;
    @ApiModelProperty(value = "房主ID")
    private String userId;
    @ApiModelProperty(value = "房间Id")
    private String roomId;

}
