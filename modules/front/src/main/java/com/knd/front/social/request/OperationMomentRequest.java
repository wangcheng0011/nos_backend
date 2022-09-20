package com.knd.front.social.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.front.dto.VoUrl;
import com.knd.front.social.dto.MomentAddressDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 发布动态对象
 */
@Data
public class OperationMomentRequest {

    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "正文")
    private String content;

    @ApiModelProperty(value = "是否公开0否 1是")
    @NotBlank(message = "是否公开不能为空")
    private String isPublic;

    @ApiModelProperty(value = "图片")
    private List<VoUrl> imageList;

    @ApiModelProperty(value = "地址信息")
    private MomentAddressDto addressDto;
}
