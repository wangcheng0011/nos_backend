package com.knd.front.user.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author zm
 */
@Data
public class PowerTestResultRequest {

    @ApiModelProperty(value = "力量测试子项结果")
    @NotEmpty(message = "力量测试子项结果不能为空")
    private List<PowerTestResultDetailRequest> detailRequestList;

    @ApiModelProperty(value = "测试用户ID")
    @NotBlank(message = "测试用户ID不能为空")
    private String userId;

}
