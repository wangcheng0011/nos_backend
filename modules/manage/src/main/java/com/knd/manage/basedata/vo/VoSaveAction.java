package com.knd.manage.basedata.vo;

import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data

public class VoSaveAction {
    //userId从token获取
    private String userId;
    @NotEmpty(message = "默认力量设置不可空")
    private List<VoAimSetting> aimSettingList;

//    @NotEmpty(message = "适宜配件不可空")
    private List<VoEquipment> equipmentList;

    @NotBlank(message = "动作名称不可空")
    @Size(max = 32, message = "最大长度为32")
    private String action;

    @NotBlank(message = "是否双臂")
    @Size(max = 1, message = "最大长度为1")
    @Pattern(regexp = "^(0|1|2)$", message = "0不添加 1单臂 2双臂")
    private String isTwoArms;

    @NotBlank(message = "动作类型id不可空")
    @Size(max = 64, message = "最大长度为64")
    private String actionType;
    //非必传
    @Size(max = 256, message = "最大长度为256")
    private String remark;

    @NotBlank(message = "目标id不可空")
    @Size(max = 64, message = "最大长度为64")
    private String targetId;

    @NotBlank(message = "部位id不可空")
    @Size(max = 64, message = "最大长度为64")
    private String partId;

    @NotBlank(message = "计数模式不可空")
    @Pattern(regexp = "^(1|2|3)$", message = "计数模式错误")
    private String countMode;

    @NotBlank(message = "访客/未测试力量等级可见 不可空")
    @Pattern(regexp = "^(10|20|30)$")
    private String powerLevel;
    @Size(max = 8, message = "最大长度为8")
    //计时
    @Pattern(regexp = "^([1-9][0-9]([0-9]{0,6})?)?", message = "计时参数错误")
    //范围是大于10，最长长度8位数
    private String aimDuration;
    @Size(max = 8, message = "最大长度为8")
    //范围是大于5，最长长度8位数  ^(([5-9]([0-9]{0,7})?)|([1-9][0-9]([0-9]{0,6})?))?
    @Pattern(regexp = "^(([5-9])|([1-9]([0-9]){1,2}))?", message = "计次参数错误")
    //计次
    private String aimTimes;
    @NotBlank(message = "基础力不可空")
    @Size(max = 8, message = "最大长度为8")
    @Pattern(regexp = "^([3-9])|([1-3][0-9])|([4][0-5])$", message = "默认基础力参数错误")
    //范围是 [3,45]
    private String basePower;
    @NotBlank(message = "力量等级测试标识不可空")
    @Pattern(regexp = "^(0|1)$", message = "力量等级测试标识错误")
    private String strengthTestFlag;
    @NotBlank(message = "操作类型不可空")
    @Pattern(regexp = "^(1|2)$", message = "操作类型错误")
    private String postType;
    @Size(max = 64, message = "最大长度为64")
    private String actionId;

    //
//    @NotBlank
    @Size(max = 256, message = "最大长度为256")
    //介绍视频原名称
    private String videoAttachName;
    //    @NotBlank
    @Size(max = 256, message = "最大长度为256")
    //介绍视频新名称
    private String videoAttachNewName;
    //    @NotBlank
    @Size(max = 32, message = "最大长度为32")
    //介绍视频大小
    private String videoAttachSize;
    //    @NotBlank
    @Size(max = 256, message = "最大长度为256")
    //封面图片原名称
    private String picAttachName;
    //    @NotBlank
    @Size(max = 256, message = "最大长度为256")
    //封面图片新名称
    private String picAttachNewName;
    //    @NotBlank
    @Size(max = 32, message = "最大长度为32")
    //封面图片大小
    private String picAttachSize;

    //自由训练标识
    @NotBlank(message = "自由训练标识不可空")
    @Pattern(regexp = "^(0|1)$", message = "自由训练标识错误")
    private String freeTrainFlag;

    @Size(max = 1, message = "最大长度为1")
    //是否仅限VIP使用
    private String vipType;

    @Size(max = 32, message = "最大长度为32")
    private String leftRightAngleOfLeft;

    @Size(max = 32, message = "最大长度为32")
    private String leftRightAngleOfRight;

    @Size(max = 32, message = "最大长度为32")
    private String upDownAngleOfLeft;

    @Size(max = 32, message = "最大长度为32")
    private String upDownAngleOfRight;

    @Size(max = 32, message = "最大长度为32")
    private String heightOfLeft;

    @Size(max = 32, message = "最大长度为32")
    private String heightOfRight;

}
