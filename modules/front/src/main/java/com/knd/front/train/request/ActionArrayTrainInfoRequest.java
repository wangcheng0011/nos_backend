package com.knd.front.train.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.LinkedList;

/**
 * @author will
 */
@Data
public class ActionArrayTrainInfoRequest {
    private String trainProgramPlanGenerationId;
    @NotBlank(message = "用户id不能为空")
    private String userId;
    @NotBlank(message = "动作序列id不能为空")
    private String actionArrayId;
    @NotBlank(message = "动作序列名称不能为空")
    private String actionArrayName;
    @NotBlank(message = "开始时间不能为空")
    private String beginTime;
    @NotBlank(message = "结束时间不能为空")
    private String endTime;
    @NotBlank(message = "运动总时间(s)不能为空")
    private String totalSeconds;
    @NotBlank(message = "实际运动总时间(s)不能为空")
    private String actTrainSeconds;
    @NotBlank(message = "完成总组数不能为空")
    private String finishSets;
    @NotBlank(message = "完成总次数不能为空")
    private String finishCounts;
    @NotBlank(message = "完成总力不能为空")
    private String finishTotalPower;
    @NotBlank(message = "设备编号不能为空")
    private String equipmentNo;
    @NotBlank(message = "最大爆发力不能为空")
    private String maxExplosiveness;
    @NotBlank(message = "平均爆发力不能为空")
    private String avgExplosiveness;
    @NotBlank(message = "卡路里不能为空")
    private String calorie;
    @Valid
    private LinkedList<TrainActionArrayItemRequest> trainActionArrayItemList;
}