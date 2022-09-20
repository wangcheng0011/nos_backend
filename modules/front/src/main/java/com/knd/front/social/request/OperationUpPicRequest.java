package com.knd.front.social.request;

import com.knd.front.dto.VoUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 上传照片
 */
@Data
public class OperationUpPicRequest {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "照片集合")
    private List<VoUrl> picList;
}
