package com.knd.manage.course.vo;


import com.knd.manage.basedata.vo.VoUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author zm
 */
@Data
public class VoSaveCoach {
    private String userId;

    @NotBlank
    @Size(max = 64)
    @ApiModelProperty(value = "教练id")
    private String id;

    @NotBlank
    @Size(max = 255)
    @ApiModelProperty(value = "描述")
    private String depict;

    @ApiModelProperty(value = "图片")
    private List<VoUrl> imageUrl;

    @NotBlank
    @Size(max = 500)
    @ApiModelProperty(value = "擅长内容")
    private String content;

    @NotBlank
    @Size(max = 500)
    @ApiModelProperty(value = "简介")
    private String synopsis;

}
