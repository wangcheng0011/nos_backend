package com.knd.front.wallpaper.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangcheng
 */
@Data
public class DelUserWallPaperRequest {
    @ApiModelProperty(value = "图片ids")
    private String ids;

}