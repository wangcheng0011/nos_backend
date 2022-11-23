package com.knd.front.wallpaper.dto;


import com.knd.front.pay.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserWallPaperDto {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "名称")
    private String wallpaperName;
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "类型 1系统壁纸 2自定义壁纸")
    private String type;
    @ApiModelProperty(value = "图片id")
    private String attachUrlId;
    @ApiModelProperty(value = "图片")
    private ImgDto imageUrl;
    @ApiModelProperty(value = "排序")
    private String sort;
    @ApiModelProperty(value = "是否选中 0 未选中 1 选中")
    private String selected;
}
