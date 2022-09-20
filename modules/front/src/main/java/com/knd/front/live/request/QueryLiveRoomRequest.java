package com.knd.front.live.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class QueryLiveRoomRequest {

    @ApiModelProperty(value = "Id")
    @NotBlank(message = "训练小组Id不能为空")
    private String trainGroupId;

    @ApiModelProperty(value = "房间状态")
    @Pattern(regexp = "^(0|1)$",message = "房间状态只能是0->正常1->关闭")
    private String roomStatus = "0";

    @NotBlank(message = "当前页不能为空")
    @ApiModelProperty(value = "当前页" ,required = true)
    private String currentPage;

}
