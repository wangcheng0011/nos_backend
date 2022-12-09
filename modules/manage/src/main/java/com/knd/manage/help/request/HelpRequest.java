package com.knd.manage.help.request;

import com.knd.manage.basedata.vo.VoUrlSort;
import com.knd.manage.basedata.vo.VoVideoUrl;
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
    //    @NotBlank
    @Size(max = 32, message = "视频id")
    //介绍视频大小
    private String videoAttachId;

    //视频
    private VoVideoUrl voVideoUrl;

    @ApiModelProperty(value = "图片")
    private List<VoUrlSort> imageUrls;

    //1新增 2修改
    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;




}