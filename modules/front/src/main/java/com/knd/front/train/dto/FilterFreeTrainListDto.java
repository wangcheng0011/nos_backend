package com.knd.front.train.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/2
 * @Version 1.0
 */
@Data
public class FilterFreeTrainListDto {
    private String actionId;
    private String action;
    private String videoAttachUrl;
    private String videoSize;
    private String picAttachUrl;
    private String countMode;
    private String aimDuration;
    private String aimTimes;
    private String basePower;
    private List<BaseEquipmentDto> targetList;
    private List<BaseEquipmentDto> partList;
    private List<BaseEquipmentDto> equipmentList;
    @ApiModelProperty(value = "左臂左右角度")
    private String leftRightAngleOfLeft;

    @ApiModelProperty(value = "右臂左右角度")
    private String leftRightAngleOfRight;

    @ApiModelProperty(value = "左臂上下角度")
    private String upDownAngleOfLeft;

    @ApiModelProperty(value = "右臂上下角度")
    private String upDownAngleOfRight;

    @ApiModelProperty(value = "左臂上下高度")
    private String heightOfLeft;

    @ApiModelProperty(value = "右臂上下高度")
    private String heightOfRight;

    @ApiModelProperty(value = "是否双臂 0不添加 1单臂 2双臂")
    private String isTwoArms;
}