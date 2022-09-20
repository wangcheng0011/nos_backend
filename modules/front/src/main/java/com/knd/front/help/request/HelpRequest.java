package com.knd.front.help.request;


import com.knd.front.dto.VoUrl;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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

    @ApiModelProperty(value = "图片")
    private List<VoUrl> imageUrls;

    //1新增 2修改
    @NotBlank
    @Pattern(regexp = "^(1|2)$")
    private String postType;




}