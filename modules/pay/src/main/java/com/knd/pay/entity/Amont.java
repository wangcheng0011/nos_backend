package com.knd.pay.entity;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class Amont {
    @NotBlank(message = "金额不能为空")
    private String total;
    @NotBlank(message = "货币类型不能为空")
    private String currency;


}
