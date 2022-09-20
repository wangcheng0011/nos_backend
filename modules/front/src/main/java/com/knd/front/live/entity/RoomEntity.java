package com.knd.front.live.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("lb_room")
@ApiModel
public class RoomEntity extends BaseEntity {

    @ApiModelProperty(value = "房主ID")
    private String userId;
    @ApiModelProperty(value = "房间名称")
    private String roomName;
    @ApiModelProperty(value = "训练小组Id")
    private String trainGroupId;
    @ApiModelProperty(value = "房间密码")
    private String invitationCode;
    @ApiModelProperty(value = "是否公开 0-》公开 1-》不公开")
    private String publicFlag;
    @ApiModelProperty(value = "房间状态 0-》直播中 1-》关闭")
    private String roomStatus;
    @ApiModelProperty(value = "简介")
    private String description;
    @ApiModelProperty(value = "人数")
    private Integer memberSize;

    @ApiModelProperty(value = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime beginDate;

    @ApiModelProperty(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime endDate;

}
