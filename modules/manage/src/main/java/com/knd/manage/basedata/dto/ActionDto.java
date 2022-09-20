package com.knd.manage.basedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ActionDto {
    //动作Id
    private String id;
    //动作名称
    private String action;
    //动作类型id
    private String actionType;
    //描述
    private String remark;
    //目标id
    private String targetId;
    //部位id
    private String partId;
    //计数模式
    private String countMode;
    //默认计数力量等级
    private String powerLevel;
    //默认计数目标时长
    private String aimDuration;
    //默认计数目标次数
    private String aimTimes;
    //默认基础力
    private String basePower;

    //力量等级测试动作标识
    private String strengthTestFlag;
    //力量等级计数目标值列表
    private List<AimSettingDto> aimSettingList;
    //适宜器材列表
    private List<EquipmentDto> equipmentList;

    //介绍视频id
//    private String videoAttachId;
    //介绍视频url
    private String videoAttachUrl;
    //封面图片id
//    private String picAttachId;
    //封面图片url
    private String picAttachUrl;

    //介绍视频原名称
    private String videoAttachName;
    //介绍视频新名称
    private String videoAttachNewName;
    //介绍视频大小
    private String videoAttachSize;
    //封面图片原名称
    private String picAttachName;
    //封面图片新名称
    private String picAttachNewName;
    //封面图片大小
    private String picAttachSize;
    //自由训练标识
    private String freeTrainFlag;
    //是否VIP使用。0否，1是
    private String vipType;

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
