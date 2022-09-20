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
@TableName("lb_room_report_attach")
@ApiModel
public class RoomReportAttachEntity extends BaseEntity {

    @ApiModelProperty(value = "举报id")
    private String reportId;

    @ApiModelProperty(value = "举报图片Id")
    private String attachUrlId;

}
