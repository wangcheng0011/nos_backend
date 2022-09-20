package com.knd.manage.course.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zm
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lb_room_report_attach")
@ApiModel(value="RoomReportAttach对象", description="")
public class RoomReportAttach extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "举报id")
    private String reportId;

    @ApiModelProperty(value = "举报图片Id")
    private String attachUrlId;

}
