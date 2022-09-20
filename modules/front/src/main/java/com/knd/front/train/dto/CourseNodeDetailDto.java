package com.knd.front.train.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/7
 * @Version 1.0
 */
@Data
public class CourseNodeDetailDto {
    private String id;
    private String nodeSort;
    private String nodeName;
    private String actionFlag;
    private String nodeBeginMinutes;
    private String nodeBeginSeconds;
    private String nodeEndMinutes;
    private String nodeEndSeconds;
    private String countDownFlag;
    private String blockId;
    private String block;
    private String actionId;
    private String action;
    private String countMode;
    private String aimDuration;
    private String aimTimes;
    private String aimSetNum;
    private String trainingFlag;
    private String endNodePeriod;
    /**
     * 开始总时长秒
     */
    private String nodeBeginTotalSeconds;
    /**
     * 结束总时长秒
     */
    private String nodeEndTotalSeconds;

    private String musicUrl;
    private String countdownLength;
    @ApiParam("休息阶段")
    private String blockRests;

    private String picAttachUrl;

    @ApiParam("是否有配件:0否 1是")
    private String isEquipment;

    @ApiModelProperty(value = "是否双臂 0不添加 1单臂 2双臂")
    private String isTwoArms;

}