package com.knd.front.live.dto;

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
@ApiModel(description = "房间列表")
public class RoomListDto {
    @ApiModelProperty(value = "主键ID",notes="主键ID")
    protected String id;
    @ApiModelProperty(value = "房主Id",notes="房主Id")
    private String userId;
    @ApiModelProperty(value = "房主昵称",notes="房主昵称")
    private String userName;
    @ApiModelProperty(value = "头像",notes="头像")
    private String headPicUrl;
    @ApiModelProperty(value = "房间名称",notes="房间名称")
    private String roomName;
    @ApiModelProperty(value = "所属小组Id",notes="所属小组Id")
    private String trainGroupId;
    @ApiModelProperty(value = "房间密码",notes="房间密码")
    private String invitationCode;
    @ApiModelProperty(value = "是否公开",notes="是否公开")
    private String publicFlag;
    @ApiModelProperty(value = "简介",notes="简介")
    private String description;
    @ApiModelProperty(value = "人数",notes="人数")
    private String memberSize;
    @ApiModelProperty(value = "作成时间",notes="作成时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime createDate;
    @ApiModelProperty(value = "房间状态 0->直播中 1->已关闭 2->未加入 3->已加入",notes="房间状态 0->直播中 1->已关闭 2->未加入 3->已加入")
    private String roomStatus;
    @ApiModelProperty(value = "开始时间",notes="开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime beginDate;


}
