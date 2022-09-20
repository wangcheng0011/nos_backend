package com.knd.front.user.request;

import com.knd.front.domain.RankingTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Lenovo
 */
@Data
public class UserTrainPraiseRequest {

    @ApiModelProperty(value = "用户id",required = true)
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty(value = "被点赞用户id",required = true)
    @NotBlank(message = "被点赞用户id不能为空")
    private String praiseUserId;

    @ApiModelProperty(value = "点赞类型",required = true)
    private RankingTypeEnum praiseType;
}
