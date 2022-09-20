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
@ApiModel(description = "申请列表")
public class ApplyListDto {
    @ApiModelProperty(value = "主键ID")
    protected String id;
    @ApiModelProperty(value = "小组名称")
    private String groupName;
    @ApiModelProperty(value = "是否审核通过 0-》待审核 1-》通过 2-》拒绝")
    private String applyFlag;
    @ApiModelProperty(value = "用户id")
    protected String userId;
    @ApiModelProperty(value = "申请用户昵称")
    private String userNickName;

    @ApiModelProperty(value = "用户头像")
    private String headPicUrl;
    @ApiModelProperty(value = "申请时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime applyDate;


}
