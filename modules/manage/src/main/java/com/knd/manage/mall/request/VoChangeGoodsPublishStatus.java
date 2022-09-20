package com.knd.manage.mall.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class VoChangeGoodsPublishStatus {
    //userId从token获取
    private String userId ;
    @NotBlank
    private String id;
    @NotBlank
    @Pattern(regexp = "^(0|1)$")
    private String status;
}
