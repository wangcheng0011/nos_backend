package com.knd.manage.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author sy
 * @since 2020-07-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("introduction_course")
@ApiModel(value="IntroductionCourse对象", description="")
public class IntroductionCourse extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "会员Id")
    private String userId;

    @ApiModelProperty(value = "课程主表Id")
    private String courseHeadId;

    @ApiModelProperty(value = "课程名称")
    private String course;

    @ApiModelProperty(value = "视频播放开始时间")
    private String vedioBeginTime;

    @ApiModelProperty(value = "视频播放结束时间")
    private String vedioEndTime;


}
