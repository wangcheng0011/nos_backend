package com.knd.manage.mall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InstallPersonDto {

    private String id;
    private String nickName;

    @ApiModelProperty(value="待安装数量")
    private String count1;

    @ApiModelProperty(value="已安装数量")
    private String count2;

    @ApiModelProperty(value="安装中数量")
    private String count3;

    @ApiModelProperty(value="最近派单")
    private LocalDateTime dateTime;
}
