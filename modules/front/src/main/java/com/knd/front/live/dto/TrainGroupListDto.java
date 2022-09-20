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
public class TrainGroupListDto {
    @ApiModelProperty(value = "主键ID")
    protected String id;
    @ApiModelProperty(value = "房主id")
    private String userId;
    @ApiModelProperty(value = "房主昵称")
    private String userName;

    @ApiModelProperty(value = "小组名称")
    private String groupName;
    @ApiModelProperty(value = "健身目标")
    private String targetName;

    @ApiModelProperty(value = "头像")
    private String headPicUrl;
//    @ApiModelProperty(value = "健身部位")
//    private String partHobbyName;
//    @ApiModelProperty(value = "房间密码")
//    private String invitationCode;
    @ApiModelProperty(value = "是否公开")
    private String publicFlag;
    @ApiModelProperty(value = "简介")
    private String description;
    @ApiModelProperty(value = "人数")
    private Integer memberSize;
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime createDate;

    @ApiModelProperty(value = "审核状态 -1-》未加入 0-》审批中 1-》已加入")
    private String applyStatus;


}
