package com.knd.manage.course.dto;

import lombok.Data;

@Data
public class NodeDto {
    //小节id
    private String id;
    //小节序号
    private String nodeSort;
    //小节名称
    private String nodeName;
    //动作标识
    private String actionFlag;
    //小节视频开始时间-分
    private String nodeBeginMinutes;
    //小节视频开始时间-秒
    private String nodeBeginSeconds;
    //小节视频结束时间-分
    private String nodeEndMinutes;
    //小节视频结束时间-秒
    private String nodeEndSeconds;
    //是否倒计时
    private String countDownFlag;
    //所属分组Id
    private String blockId;
    //所属分组名称
    private String block;
    //所属动作id
    private String actionId;
    //所属动作名称
    private String action;
    //计数模式
    private String countMode;
    //计数目标时长
    private String aimDuration;
    //计数目标次数
    private String aimTimes;
    //训练动作标识，0：不是训练动作，1：是训练动作
    private String trainingFlag;
    //完结动作时长
    private String endNodePeriod;

    //音乐id
    private String musicId;
    //倒计时长
    private String countdownLength;
    //休息阶段
    private String blockRests;






}
