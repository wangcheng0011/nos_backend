package com.knd.front.wallpaper.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wangcheng
 */
@Data
public class SaveUserWallPaperRequest {

    @ApiModelProperty(value = "壁纸id")
    private List<UserWallPaper> userWallPaperList;



}