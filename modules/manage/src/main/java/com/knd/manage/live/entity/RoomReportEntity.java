package com.knd.manage.live.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

    @ApiModelProperty(value = "举报类型只能是0->小组房间1->直播课2->私教课")
    private String type;

}
