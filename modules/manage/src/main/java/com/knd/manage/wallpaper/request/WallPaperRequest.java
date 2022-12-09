package com.knd.manage.wallpaper.request;


import com.knd.manage.basedata.vo.VoUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author wangcheng
 */
@Data
public class WallPaperRequest {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "图片")
    private VoUrl picAttachUrl;

    @ApiModelProperty(value = "壁纸名称")
    private String wallpaperName;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "type=1系统壁纸 type=2自定义壁纸 type=3运动数据壁纸")
    private String type;

    //1新增 2修改
    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;

    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "是否选中 0 未选中 1 选中")
    private String selected;


}