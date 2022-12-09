package com.knd.manage.wallpaper.dto;


import com.knd.manage.basedata.dto.ImgDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WallPaperDto {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "排序")
    private String sort;
    @ApiModelProperty(value = "是否选中 0 未选中 1 选中")
    private String selected;
    @ApiModelProperty(value = "壁纸名称")
    private String wallpaperName;
    @ApiModelProperty(value = "类型 1系统壁纸 2自定义壁纸 3运动数据壁纸")
    private String type;
    @ApiModelProperty(value = "图片")
    private ImgDto imageUrl;
}
