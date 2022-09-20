package com.knd.manage.course.vo;


import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class VoSaveCourseNodeInfo {

    //userId从token获取
    private String userId;

    @NotBlank(message = "课程id不可空")
    @ApiParam("课程id")
    @Size(max = 64, message = "最大长度为64")
    private String courseId;

    @NotBlank(message = "小节序号不可空")
    @ApiParam("小节序号")
    @Size(max = 8, message = "最大长度为8")
    private String nodeSort;

    @NotBlank(message = "小结名称不可空")
    @Size(max = 64, message = "最大长度为64")
    @ApiParam("小结名称")
    private String nodeName;

    @NotBlank(message = "动作标识不可空")
    @Pattern(regexp = "^(0|1)$", message = "动作标识参数错误")
    @ApiParam("动作标识,0-不是 1-是")
    private String actionFlag;

    @NotBlank(message = "小节视频开始时间-分不可空")
    @ApiParam("小节视频开始时间-分")
    private String nodeBeginMinutes;

    @NotBlank(message = "小节视频开始时间-秒不可空")
    @ApiParam("小节视频开始时间-秒")
    private String nodeBeginSeconds;

    @NotBlank(message = "小节视频结束时间-分不可空")
    @ApiParam("小节视频结束时间-分")
    private String nodeEndMinutes;

    @NotBlank(message = "小节视频结束时间-秒不可空")
    @ApiParam("小节视频结束时间-秒")
    private String nodeEndSeconds;

    @NotBlank(message = "是否倒计时参数不可空")
    @Pattern(regexp = "^(0|1)$", message = "是否倒计时参数错误")
    @ApiParam("是否倒计时,0-不需要 1-需要")
    private String countDownFlag;

    @ApiParam("所属分组Id")
    private String blockId;

    @ApiParam("所属动作id")
    private String actionId;

    @Size(max = 8, message = "最大长度为8")
    @ApiParam("计数目标时长")
    private String aimDuration;

    @Size(max = 8, message = "最大长度为8")
    @ApiParam("计数目标次数")
    @Pattern(regexp = "^([5-9])|([1-9]([0-9]){1,2})", message = "计次参数错误")
    private String aimTimes;

    @NotBlank(message = "操作不可空")
    @ApiParam("操作类型,1新增，2更新")
    @Pattern(regexp = "^(1|2)$", message = "操作类型错误")
    private String postType;

    @ApiParam("课程小节Id,更新时必传")
    private String id;

    @Pattern(regexp = "^(0|1)$", message = "维护训练动作目标值错误")
    @ApiParam("维护训练动作目标值,0:不是动作，1：是真动作")
    private String trainingFlag;

    @ApiParam("完结动作时长（该时间段内的视频不参与循环播放），只要有计数模式都需要必传")
    private String endNodePeriod;

    @ApiParam("音乐id")
    private String musicId;

    @ApiParam("倒计时长")
    private String countdownLength;

    @ApiParam("休息阶段")
    private String blockRests;

}
