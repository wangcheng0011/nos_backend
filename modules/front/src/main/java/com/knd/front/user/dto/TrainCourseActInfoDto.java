package com.knd.front.user.dto;

import lombok.Data;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/3
 * @Version 1.0
 */
@Data
public class TrainCourseActInfoDto {
    private String actId;
    private String actCountMod;
    private String action;
    private String actTotalSeconds;
    private String actBasePowerSetting;
    private String actFinishSets;
    private String actAimSets;
    private String actAimDuration;
    private String actTotalPower;
}