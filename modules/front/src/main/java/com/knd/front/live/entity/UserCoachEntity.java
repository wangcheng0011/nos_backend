package com.knd.front.live.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 教练表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lb_user_coach")
@ApiModel(value="UserCoachEntity对象", description="")
public class UserCoachEntity extends BaseEntity {

    @ApiModelProperty(value = "教练用户id")
    private String userId;

    @ApiModelProperty(value = "擅长内容")
    private String content;

    @ApiModelProperty(value = "简介")
    private String synopsis;

    @ApiModelProperty(value = "教练描述")
    private String depict;

    @ApiModelProperty(value = "课前咨询单价")
    private BigDecimal consultPrice;

    @ApiModelProperty(value = "私教课程单价")
    private BigDecimal coursePrice;

    @ApiModelProperty(value = "直播课程单价")
    private BigDecimal livePrice;

    @ApiModelProperty(value = "学员人数")
    private long traineeNum;

}
