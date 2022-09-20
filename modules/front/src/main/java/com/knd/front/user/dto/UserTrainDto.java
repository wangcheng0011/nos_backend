package com.knd.front.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
public class UserTrainDto {
    @ApiModelProperty(value = "人员id")
    private String userId;

    @ApiModelProperty(value = "人员姓名")
    private String nickName;

    @ApiModelProperty(value = "爆发力、毅力、总力量数值")
    private String trainNum;

    @ApiModelProperty(value = "是否点赞0是1否")
    private String praise;

    @ApiModelProperty(value = "被点赞数量")
    private String praiseNum;

    @ApiModelProperty(value = "头像URL")
    private String headPicUrl;
}
