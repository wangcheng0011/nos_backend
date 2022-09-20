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
@ApiModel(description = "小组成员列表")
public class GroupUserListDto {
    @ApiModelProperty(value = "主键ID",notes = "主键ID")
    protected String id;
    @ApiModelProperty(value = "小组名称",notes = "小组名称")
    private String groupName;
    @ApiModelProperty(value = "是否0-》否 1-》是",notes = "是否管理员0-》否 1-》是")
    private String isAdmin;
    @ApiModelProperty(value = "用户id",notes = "用户昵称")
    protected String userId;
    @ApiModelProperty(value = "用户昵称",notes = "主键ID")
    private String userNickName;
    @ApiModelProperty(value = "用户头像",notes = "用户头像")
    private String headPicUrl;
    @ApiModelProperty(value = "加入时间",notes = "加入时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime joinDate;


}
