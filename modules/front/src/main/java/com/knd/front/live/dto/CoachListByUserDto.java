package com.knd.front.live.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 教练列表dto
 */
@Data
public class CoachListByUserDto {

    @ApiModelProperty(value = "标签")
    private List<String> labelList;

    @ApiModelProperty(value = "教练id")
    private String coachId;

    @ApiModelProperty(value = "教练用户id")
    private String coachUserId;

    @ApiModelProperty(value = "教练姓名")
    private String coachName;

    @ApiModelProperty(value = "教练头像url")
    private String coachHeadUrl;

    @ApiModelProperty(value = "教练描述")
    private String depict;


}
