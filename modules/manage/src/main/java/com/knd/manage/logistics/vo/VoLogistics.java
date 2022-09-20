package com.knd.manage.logistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;
@Builder
@Data
public class VoLogistics {
    //userId从token获取
    private String userId;
    @Size(max = 64)
    @ApiModelProperty("运单号")
    private String trackingNumber;

    @ApiModelProperty("物流公司 1德邦 2安能")
    private String logisticsCompanies;




}
