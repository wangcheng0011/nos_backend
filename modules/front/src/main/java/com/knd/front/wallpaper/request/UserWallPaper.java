package com.knd.front.wallpaper.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
@Data
public class UserWallPaper {
    @ApiModelProperty(value = "壁纸id")
    @NotBlank
    private String wallpaperId;

/*    @ApiModelProperty(value = "用户id")
    //userId从token获取
    private String userId;*/
}
