package com.knd.front.live.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zm
 */
@Data
@ApiModel(description = "直播列表")
public class LiveIngListDto {

    @ApiModelProperty(value = "课程时间id")
    private String timeId;

    @ApiModelProperty(value = "课程名称")
    protected String courseName;

    @ApiModelProperty(value = "课程时间（分）")
    private String courseTime;

    @ApiModelProperty(value = "能量消耗（千卡）")
    private String consume;

    @ApiModelProperty(value = "封面图url")
    private String picAttachUrl;

    @ApiModelProperty(value = "难度")
    private String difficulty;

    @ApiModelProperty(value = "是否已预约0否 1是")
    private String isOrder;

    @ApiModelProperty(value = "教练用户id")
    private String coachUserId;

    @ApiModelProperty(value = "教练头像url")
    private String coachHeadUrl;

    @ApiModelProperty(value = "教练姓名")
    private String coachName;

    @ApiModelProperty(value = "预约人数")
    private int orderNum;

    @ApiModelProperty(value = "课程类型：0课前咨询 1私教课程 2团课")
    private String courseType;

    @ApiModelProperty(value = "训练目标")
    private List<String> targetList;

    @ApiModelProperty(value = "训练部位")
    private List<String> partList;

}
