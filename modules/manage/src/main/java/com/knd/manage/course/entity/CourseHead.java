package com.knd.manage.course.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("course_head")
@ApiModel(value="CourseHead对象", description="")
public class CourseHead extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "课程名称")
    private String course;

    @ApiModelProperty(value = "训练课程标识")
    private String trainingFlag;

    @ApiModelProperty(value = "视频介绍描述")
    private String remark;

    @ApiModelProperty(value = "适用人群介绍")
    private String fitCrowdRemark;

    @ApiModelProperty(value = "禁忌人群介绍")
    private String unFitCrowdRemark;

    @ApiModelProperty(value = "是否大屏显示")
    private String appHomeFlag;

    @ApiModelProperty(value = "显示顺序")
    private String sort;

    @ApiModelProperty(value = "频率")
    private String frequency;

    @ApiModelProperty(value = "适用会员范围")
    private String userScope;

    @ApiModelProperty(value = "介绍视频id")
    private String videoAttachId;

    @ApiModelProperty(value = "介绍视频名称")
    private String videoAttachName;

    @ApiModelProperty(value = "封面图片id")
    private String picAttachId;

    @ApiModelProperty(value = "封面图片url")
    private String picAttachUrl;

    @ApiModelProperty(value = "视频总时长-分钟")
    private String videoDurationMinutes;

    @ApiModelProperty(value = "视频总时长-秒")
    private String videoDurationSeconds;

    @ApiModelProperty(value = "发布状态")
    private String releaseFlag;

    @ApiModelProperty(value = "发布时间")
    private String releaseTime;

    private BigDecimal amount;

    private String courseType;

    private String difficultyId;




}
