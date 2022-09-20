package com.knd.front.train.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/8
 * @Version 1.0
 */
@Data
public class TrainFreeItemRequest {
    @NotBlank(message = "动作id不能为空")
    private String actId;
    @NotBlank(message = "动作计数模式不能为空")
    private String actCountMod;
    @NotBlank(message = "动作名称不能为空")
    private String action;
    @NotBlank(message = "动作组序号不能为空")
    private String actSetNum;
    @NotBlank(message = "动作训练时间不能为空")
    private String actTotalSeconds;
    @NotBlank(message = "动作最后设置力不能为空")
    private String actLastPowerSetting;
    @NotBlank(message = "动作完成数不能为空")
    private String actFinishCounts;
    @NotBlank(message = "动作目标数不能为空")
    private String actAimCounts;
    @NotBlank(message = "动作目标时间不能为空")
    private String actAimDuration;
    @NotBlank(message = "完成总力不能为空")
    private String finishTotalPower;
}