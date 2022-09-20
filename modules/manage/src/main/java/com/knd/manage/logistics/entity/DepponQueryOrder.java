package com.knd.manage.logistics.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class DepponQueryOrder {
    @Size(max = 64)
    @ApiModelProperty("渠道单号 渠道单号 String 32 是 由第三⽅接⼊商产⽣的订单号（⽣成规则为sign+数字，sign值由双⽅约定）")
    private String logisticID;
    @Size(max = 64)
    @ApiModelProperty("物流公司ID")
    private String logisticCompanyID = "DEPPON";


}
