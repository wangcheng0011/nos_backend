package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author will
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("action_array")
@ApiModel(value="ActionArray对象", description="")
public class ActionArray extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "动作组合名称")
    private String actionArrayName;

    @ApiModelProperty(value = "封面资源ID")
    private String coverAttachId;

    @ApiModelProperty(value = "动作数量")
    private String actionQuantity;

    @ApiModelProperty(value = "总时长")
    private String totalDuration;

    @ApiModelProperty(value = "共享状态")
    private String shareStatus;


    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "会员Id")
    private String userId;

/*    @ApiModelProperty(value = "封面资源ID")
    private String coverAttachUrl;

    @ApiModelProperty(value = "视频资源URL集合")
    private List<VideoUrlDto> actionVideoUrl;

    @ApiModelProperty(value = "创建时间")
    protected LocalDateTime createDate;*/

}
