package com.knd.front.social.dto;

import com.knd.common.em.FollowStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class UserInfoDto {

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "头像url")
    private String headPicUrl;

    @ApiModelProperty(value = "关注状态")
    private FollowStatusEnum followStatus;

    @ApiModelProperty(value = "是否是vip")
    private String isVip;

    @ApiModelProperty(value = "是否是教练0否 1是")
    private String isCoach;

    @ApiModelProperty(value = "粉丝数量")
    private int fansNum;

    @ApiModelProperty(value = "关注数")
    private int followNum;

    @ApiModelProperty(value = "点赞数")
    private long thumbupNum;

    @ApiModelProperty(value = "标签信息")
    private List<String> LabelList;
}
