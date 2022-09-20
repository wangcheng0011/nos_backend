package com.knd.manage.user.dto;

import lombok.Data;

import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/8/17
 * @Version 1.0
 */
@Data
public class TrainFreeHeadDto {

    private String commitTrainTime;
    private String action;
    private String totalSeconds;
    private String actTrainSeconds;
    private String finishSets;
    private String finishCounts;
    private String finishTotalPower;
    private List<TrainFreeItemsDto> trainFreeItemList;


}