package com.knd.front.train.dto;

import com.knd.front.entity.BaseBodyPart;
import lombok.Data;

import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@Data
public class ActionDto {
    private String actionId;
    private String action;
    private String countMode;
    private String aimDuration;
    private String aimTimes;
    private String nodeName;
    private String nodeId;
    private String actionType;
    private String basePower;
    private List<BaseBodyPart> partList;
    private String picAttachUrl;
}