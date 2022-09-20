package com.knd.front.live.request;

import com.knd.front.dto.VoUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author zm
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@Data
public class ReportRoomRequest {

    @ApiModelProperty(value = "举报类型")
    @Pattern(regexp = "^(0|1|2)$",message = "举报类型只能是0->小组房间1->直播课2->私教课")
    private String type;

    @ApiModelProperty(value = "房间id",required = true)
    @Size(max = 64)
    @NotBlank
    private String roomId;

    @ApiModelProperty(value = "举报人员Id",required = true)
    @Size(max = 64)
    @NotBlank
    private String reportUserId;

    @ApiModelProperty(value = "举报内容")
    @Size(max = 500)
    private String content;

    @ApiModelProperty(value = "举报描述")
    @Size(max = 1000)
    private String represent;

    @ApiModelProperty(value = "举报图片")
    private List<VoUrl> attachUrlList;

}