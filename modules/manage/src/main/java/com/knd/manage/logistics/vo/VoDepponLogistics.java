package com.knd.manage.logistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class VoDepponLogistics {
    @Size(max = 64)
    @ApiModelProperty("运单号")
    private String mailNo;




}
