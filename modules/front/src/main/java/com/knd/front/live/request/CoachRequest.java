package com.knd.front.live.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author zm
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@Data
public class CoachRequest {
    @ApiModelProperty(value = "教练类型：0健美 1力量 2瑜伽 3舞蹈 4普拉提 5体能")
    private List<String> typeList;

    @ApiModelProperty(value = "性别：1男 2女")
    private String sex;

    @ApiModelProperty(value = "排序：0服务学员人数高到低 1价格低到高 2价格高到低")
    private String sort;

    @ApiModelProperty(value = "当前页")
    @NotBlank
    private String current;;

}