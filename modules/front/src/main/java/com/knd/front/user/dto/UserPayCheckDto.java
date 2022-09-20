package com.knd.front.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserPayCheckDto {
    @ApiModelProperty(value = "类型：0课程")
    private Integer type;

    @ApiModelProperty(value = "关联主键id")
    private String payId;

    @ApiModelProperty(value = "结果: 0支付 1未支付")
    private Boolean result;
}
