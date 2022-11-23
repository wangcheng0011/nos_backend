package com.knd.front.train.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.LinkedList;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/7
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrainCourseInfoRequest {
    private String trainProgramPlanGenerationId;
    private String userId;
    @NotBlank(message = "课程主表id不能为空")
    private String courseHeadId;
    @NotBlank(message = "课程名不能为空")
    private String course;
    @NotBlank(message = "视频开始时间不能为空")
    private String vedioBeginTime;
    @NotBlank(message = "视频结束时间不能为空")
    private String vedioEndTime;
    @NotBlank(message = "运动总时间(秒)不能为空")
    private String totalDurationSeconds;
    @NotBlank(message = "实际运动总时间（秒）不能为空")
    private String actualTrainSeconds;
    @NotBlank(message = "设备编号不能为空")
    private String equipmentNo;
    @NotBlank(message = "最大爆发力不能为空")
    private String maxExplosiveness;
    @NotBlank(message = "平均爆发力不能为空")
    private String avgExplosiveness;
    @NotBlank(message = "卡路里不能为空")
    private String calorie;
    @NotBlank(message = "训练总力不能为空")
    private String finishTotalPower;
    @Valid
    private LinkedList<TrainCourseBlockInfoRequest> blockList;

}