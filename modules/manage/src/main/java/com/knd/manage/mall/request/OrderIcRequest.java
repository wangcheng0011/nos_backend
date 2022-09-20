package com.knd.manage.mall.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderIcRequest {

    @ApiModelProperty(value = "安装位置")
    private String installPosition;

    @ApiModelProperty(value = "安装方式")
    private String mountType;

    @ApiModelProperty(value = "墙体结构")
    private String wallStructure;

    @ApiModelProperty(value = "墙面材质")
    private String wallMaterial;

    @ApiModelProperty(value = "地面材质")
    private String floorMaterial;

    @ApiModelProperty(value = "电源距离")
    private String powerDistance;

    @ApiModelProperty(value = "是否有电梯0是，1否")
    private String isElevator;

    @ApiModelProperty(value = "是否布线0是，1否")
    private String isWiring;

    @ApiModelProperty(value = "布线要求")
    private String wiringRequire;

    @ApiModelProperty(value = "安装开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime installationBeginTime;

    @ApiModelProperty(value = "安装结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime installationEndTime;
}
