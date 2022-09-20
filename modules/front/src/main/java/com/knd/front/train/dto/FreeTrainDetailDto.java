package com.knd.front.train.dto;

import com.knd.front.home.dto.BaseTargetDto;
import lombok.Data;

import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/7
 * @Version 1.0
 */
@Data
public class FreeTrainDetailDto {
    private String actionId;
    private String actionType;
    private String action;
    private String videoAttachUrl;
    private String videoSize;
    private String picAttachUrl;
    private List<BaseTargetDto> targetList;
    private List<BaseTargetDto> partList;
    private String countMode;
    private String aimDuration;
    private String aimTimes;
    private String basePower;
    private String isTwoArms;

}