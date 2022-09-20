package com.knd.front.user.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/3
 * @Version 1.0
 */
@Data
public class UserCenterInfoDto {
    private String nickName;
    private String trainLevel;
    private String trainPeriodBeginTime;
    private String totalSeconds;
    private String actTrainSeconds;
    private String latestTrainLevel;
    private String totalTrainKg;
    private String totalCalorie;
    private String perSign;
    private String bmi;
    private String testTrain;
    private String masterId;
    private String vipStatus;
    private LocalDate vipBeginDate;
    private LocalDate vipEndDate;
    private String headPicUrl;
    private String isCoach;
}