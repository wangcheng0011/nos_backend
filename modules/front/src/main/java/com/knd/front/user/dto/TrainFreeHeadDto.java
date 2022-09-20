package com.knd.front.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/3
 * @Version 1.0
 */
@Data
public class TrainFreeHeadDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime commitTrainTime;
    private String action;
    private String totalSeconds;
    private String actTrainSeconds;
    private String finishSets;
    private String finishCounts;
    private String finishTotalPower;
    private String maxExplosiveness;
    private String avgExplosiveness;
    private String calorie;
    private List<TrainFreeItemsDto> trainFreeItemList;
}