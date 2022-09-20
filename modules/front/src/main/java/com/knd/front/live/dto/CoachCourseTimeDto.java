package com.knd.front.live.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "教练课程计划")
public class CoachCourseTimeDto {

    @ApiModelProperty(value = "课程时间id")
    private String id;

    @ApiModelProperty(value = "课程名称")
    protected String courseName;

    @ApiModelProperty(value = "课程开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime beginTime;

    @JsonIgnore
    @ApiModelProperty(value = "直播状态分类: 0-未开始，1-直播中 2-已完成")
    protected String liveStatus;

    @JsonIgnore
    @ApiModelProperty(value = "预约状态: 0-预约中，1-已预约 2-已取消")
    protected String isOrder;

    @ApiModelProperty(value = "直播课状态分类: 0-查询所有，1-最新预约 2-待上，3-已完成")
    protected String status;

    @ApiModelProperty(value = "约课用户id")
    private String userId;

    @ApiModelProperty(value = "约课用户性别")
    private String gender;

    @ApiModelProperty(value = "约课用户性别")
    private String birthDay;

    @ApiModelProperty(value = "约课用户头像")
    private String headPicUrl;

    @ApiModelProperty(value = "约课用户昵称")
    private String nickName;

    @ApiModelProperty(value = "约课用户手机号")
    private String mobile;

}
