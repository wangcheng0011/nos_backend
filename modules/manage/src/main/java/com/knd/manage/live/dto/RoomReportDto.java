package com.knd.manage.live.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.manage.basedata.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Lenovo
 */
@Data
public class RoomReportDto {

    private String id;

    @ApiModelProperty(value = "房间id")
    private String roomId;

    @ApiModelProperty(value = "房间名称")
    private String room;

    @ApiModelProperty(value = "UserId")
    private String userId;

    @ApiModelProperty(value = "房主昵称")
    private String roomUserName;

    @ApiModelProperty(value = "房主手机号")
    private String roomMobile;

    @ApiModelProperty(value = "举报人员昵称")
    private String reportUserName;

    @ApiModelProperty(value = "举报人员手机号")
    private String reportUserMoblie;

    @ApiModelProperty(value = "举报内容")
    private String content;

    @ApiModelProperty(value = "举报描述")
    private String represent;

    @ApiModelProperty(value = "举报图片")
    private List<ImgDto> imageUrl;

    @ApiModelProperty(value = "是否冻结用户 0非冻结 1冻结")
    private String isFrozen;

    @ApiModelProperty(value = "是否关闭房间")
    private String isClose;

    @ApiModelProperty(value = "是否踢出小组")
    private String isKickOut;

    @ApiModelProperty(value = "举报类型只能是0->小组房间1->直播课2->私教课类型")
    private String type;

    @ApiModelProperty(value = "举报时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime reportTime;

    @ApiModelProperty(value = "开播时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    protected LocalDateTime beginTime;

}
