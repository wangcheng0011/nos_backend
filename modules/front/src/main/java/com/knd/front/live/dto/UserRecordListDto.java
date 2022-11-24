package com.knd.front.live.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author zm
 *
 */
@Data
@ApiModel(description = "用户消息记录列表")
public class UserRecordListDto {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "预约名称")
    private String orderName;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "预约类别0课前咨询 1私教课程 2团课直播 3小组 4训练计划")
    private String orderType;

    @ApiModelProperty(value = "时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    protected LocalDateTime orderTime;
  /*  @ApiModelProperty(value = "时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime orderTime;*/

    @ApiModelProperty(value = "关联id")
    private String relevancyId;

    @ApiModelProperty(value = "是否已读0否 1是")
    private String isRead;


}
