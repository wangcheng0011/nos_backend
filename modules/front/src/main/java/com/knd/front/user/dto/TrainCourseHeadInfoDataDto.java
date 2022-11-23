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
public class TrainCourseHeadInfoDataDto {
    private String id;
    private String course;
    private String totalDurationSeconds;
    private String actualTrainSeconds;
    private String finishTotalPower;
    private String maxExplosiveness;
    private String avgExplosiveness;
    private String calorie;
    private String finishCounts;

}