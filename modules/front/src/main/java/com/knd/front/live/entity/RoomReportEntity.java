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
@TableName("lb_room_report")
@ApiModel
public class RoomReportEntity extends BaseEntity {

    @ApiModelProperty(value = "房间id")
    private String roomId;

    @ApiModelProperty(value = "举报人员Id")
    private String reportUserId;

    @ApiModelProperty(value = "举报内容")
    private String content;

    @ApiModelProperty(value = "举报描述")
    private String represent;

    @ApiModelProperty(value = "举报类型")
    private String type;

    @ApiModelProperty(value = "开播时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime beginTime;

    @ApiModelProperty(value = "开播时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime reportTime;

}
