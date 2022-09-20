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
public class TrainFreeItemsDto {
    private String actId;
    private String actCountMod;
    private String action;
    private String actSetNum;
    private String actTotalSeconds;
    private String actLastPowerSetting;
    private String actFinishCounts;
    private String actAimCounts;
    private String actAimDuration;
    private String finishTotalPower;
}