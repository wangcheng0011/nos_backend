package com.knd.front.train.dto;

import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @author will
 */
@Data
@ApiModel(value="ActionArray", description="")
public class ActionArrayDto extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "动作组合名称")
    private String actionArrayName;

    @ApiModelProperty(value = "封面资源ID")
    private String coverAttachUrl;

    @ApiModelProperty(value = "视频资源URL集合")
    private List<VideoUrlDto> actionVideoUrl;


    @ApiModelProperty(value = "动作数量")
        private String actionQuantity;

    @ApiModelProperty(value = "总时长")
    private String totalDuration;

    @ApiModelProperty(value = "共享状态")
    private String shareStatus;


    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "用户Id")
    private String userId;

}
