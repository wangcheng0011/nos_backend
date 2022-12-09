package com.knd.front.help.request;


import com.knd.front.dto.VoUrlSort;
import com.knd.front.dto.VoVideoUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;


/**
 * @author wangcheng
 */
@Data
public class HelpRequest {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "标题排序")
    private String titleSort;

    @Size(max = 32, message = "视频id")
    //介绍视频大小
    private String videoAttachId;
    //    @NotBlank
    @Size(max = 256, message = "最大长度为256")
    //介绍视频原名称
    private VoVideoUrl voVideoUrl;

    @ApiModelProperty(value = "图片")
    private List<VoUrlSort> imageUrls;

    //1新增 2修改
    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;




}