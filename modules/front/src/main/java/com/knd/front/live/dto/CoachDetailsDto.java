package com.knd.front.live.dto;

import com.knd.common.em.FollowStatusEnum;
import com.knd.front.entity.User;
import com.knd.front.pay.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 教练主页dto
 */
@Data
public class CoachDetailsDto {
    @ApiModelProperty(value = "学员用户")
    private List<User> orderUsers;

    @ApiModelProperty(value = "教练姓名")
    private String coachName;

    @ApiModelProperty(value = "教练头像url")
    private String coachHeadUrl;

    @ApiModelProperty(value = "关注状态")
    private FollowStatusEnum followStatus;

    @ApiModelProperty(value = "标签信息")
    private List<String> LabelList;

    @ApiModelProperty(value = "粉丝数量")
    private int fansNum;

    @ApiModelProperty(value = "关注数")
    private int followNum;

    @ApiModelProperty(value = "点赞数")
    private long thumbupNum;

    @ApiModelProperty(value = "学员人数")
    private long traineeNum;

    @ApiModelProperty(value = "课前咨询单价")
    private BigDecimal consultPrice;

    @ApiModelProperty(value = "私教课程单价")
    private BigDecimal coursePrice;

    @ApiModelProperty(value = "直播课程单价")
    private BigDecimal livePrice;

    @ApiModelProperty(value = "擅长内容")
    private String content;

    @ApiModelProperty(value = "简介")
    private String synopsis;

    @ApiModelProperty(value = "教练描述")
    private String depict;

    @ApiModelProperty(value = "图片")
    private List<ImgDto> imageUrl;
}
