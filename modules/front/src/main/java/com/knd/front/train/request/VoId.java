package com.knd.front.train.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author zm
 */
@Data
public class VoId {
    /**
     * userId从token获取
     */
    private String userId ;

    @NotBlank
    @Size(max = 64)
    @ApiModelProperty(value = "力量测试主键id")
    private String id;
}
