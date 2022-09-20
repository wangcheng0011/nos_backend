package com.knd.front.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("tb_order_ic")
public class OrderIcEntity {

    private String id;

    @ApiModelProperty(value = "订单id")
    private String tbOrderId;

    @ApiModelProperty(value = "安装位置")
    private String installPosition;

    @ApiModelProperty(value = "墙体结构")
    private String wallStructure;

    @ApiModelProperty(value = "安装类型")
    private String mountType;

    @ApiModelProperty(value = "电源距离")
    private String powerDistance;

    @ApiModelProperty(value = "是否有电梯")
    private String isElevator;

    @ApiModelProperty(value = "安装地址")
    private String address;

    @ApiModelProperty(value = "安装开始时间")
    private LocalDateTime installationBeginTime;

    @ApiModelProperty(value = "安装结束时间")
    private LocalDateTime installationEndTime;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "地面材质")
    private String floorMaterial;

    @ApiModelProperty(value = "安装区域id")
    private String areaId;

    @ApiModelProperty(value = "安装人员id")
    private String personId;





}
