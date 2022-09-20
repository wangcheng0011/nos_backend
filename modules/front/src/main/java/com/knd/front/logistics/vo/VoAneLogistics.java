package com.knd.front.logistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
@Data
public class VoAneLogistics {
    @Size(max = 64)
    @ApiModelProperty("运单号")
    private String ewbNo;




}
