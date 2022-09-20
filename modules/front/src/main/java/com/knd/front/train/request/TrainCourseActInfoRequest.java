package com.knd.front.train.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/7
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrainCourseActInfoRequest {
    @NotBlank(message = "动作id不能为空")
    private String actId;
    @NotBlank(message = "动作计数模式不能为空")
    private String actCountMod;
    @NotBlank(message = "动作名称不能为空")
    private String action;
    @NotBlank(message = "动作总时间不能为空")
    private String actTotalSeconds;
    @NotBlank(message = "动作基础力不能为空")
    private String actBasePowerSetting;
    @NotBlank(message = "动作完成组数不能为空")
    private String actFinishSets;
//    @NotBlank(message = "动作目标组数不能为空")
    private String actAimSets;
//    @NotBlank(message = "动作目标时间不能为空")
    private String actAimDuration;
    @NotBlank(message = "动作总力不能为空")
    private String actTotalPower;
}