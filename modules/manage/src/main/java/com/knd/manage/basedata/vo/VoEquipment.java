package com.knd.manage.basedata.vo;


import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
//@NoArgsConstructor                 //无参构造
//@AllArgsConstructor                //有参构造
public class VoEquipment {
    @ApiModelProperty(value = "器材id")
    @NotBlank(message = "器材id不可空")
    private String equipmentId;

    @ApiModelProperty(value = "器材名字")
    @NotBlank(message = "器材名称不可空")
    private String equipment;


}
