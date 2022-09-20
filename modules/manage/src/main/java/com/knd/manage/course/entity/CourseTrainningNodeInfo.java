package com.knd.manage.course.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Pattern;

/**
 * <p>
 *
 * </p>
 *
 * @author sy
 * @since 2020-07-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("course_trainning_node_info")
@ApiModel(value = "CourseTrainningNodeInfo对象", description = "")
public class CourseTrainningNodeInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "课程id")
    private String courseId;

    @ApiModelProperty(value = "小节序号")
    private String nodeSort;

    @ApiModelProperty(value = "小节名称")
    private String nodeName;

    @ApiModelProperty(value = "动作标识")
    private String actionFlag;
    @Pattern(regexp = "^[0-9]([0-9]*)?")
    @ApiModelProperty(value = "小节视频开始时间-分")
    private String nodeBeginMinutes;
    @Pattern(regexp = "^([0-5])?[0-9]")
    @ApiModelProperty(value = "小节视频开始时间-秒")
    private String nodeBeginSeconds;
    @Pattern(regexp = "^[0-9]([0-9]*)?")
    @ApiModelProperty(value = "小节视频结束时间-分")
    private String nodeEndMinutes;
    @Pattern(regexp = "^([0-5])?[0-9]")
    @ApiModelProperty(value = "小节视频结束时间-秒")
    private String nodeEndSeconds;

    @ApiModelProperty(value = "是否倒计时")
    private String countDownFlag;

    @ApiModelProperty(value = "所属分组Id")
    private String blockId;

    @ApiModelProperty(value = "所属动作id")
    private String actionId;

    @ApiModelProperty(value = "计数目标时长")
    private String aimDuration;

    @ApiModelProperty(value = "计数目标次数")
    private String aimTimes;

    @ApiModelProperty(value = "维护训练动作目标值,0:不是动作，1：是真动作")
    private String trainingFlag;

    @ApiModelProperty(value = "完结动作时长（该时间段内的视频不参与循环播放）")
    private String endNodePeriod;

    @ApiModelProperty("音乐id")
    private String musicId = "";

    @ApiModelProperty("倒计时长")
    private String countdownLength = "";

    @ApiParam("休息阶段")
    private String blockRests = "";

}
