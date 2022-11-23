package com.knd.front.wallpaper.request;

import com.knd.front.dto.VoUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author wangcheng
 */
@Data
public class UpdateWallPaperRequest {

    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "图片")
    private VoUrl picAttachUrl;

    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "是否选中 0 未选中 1 选中")
    private String selected;




}