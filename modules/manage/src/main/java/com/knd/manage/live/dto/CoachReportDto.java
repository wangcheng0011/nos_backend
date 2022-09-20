package com.knd.manage.live.dto;

import com.knd.manage.basedata.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Lenovo
 */
@Data
public class CoachReportDto {

    private String id;

    @ApiModelProperty(value = "timeId")
    private String timeId;

    @ApiModelProperty(value = "私教名称")
    private String coachName;

    @ApiModelProperty(value = "UserId")
    private String userId;

    @ApiModelProperty(value = "房主昵称")
    private String roomUserName;

    @ApiModelProperty(value = "举报人员昵称")
    private String reportUserName;

    @ApiModelProperty(value = "举报内容")
    private String content;

    @ApiModelProperty(value = "举报描述")
    private String represent;

    @ApiModelProperty(value = "举报图片")
    private List<ImgDto> imageUrl;

    @ApiModelProperty(value = "是否冻结用户 0非冻结 1冻结")
    private String isFrozen;

    @ApiModelProperty(value = "是否关闭房间")
    private String isClose;

    @ApiModelProperty(value = "是否踢出小组")
    private String isKickOut;

}
