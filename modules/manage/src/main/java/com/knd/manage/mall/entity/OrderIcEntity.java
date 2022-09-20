package com.knd.manage.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@TableName("tb_order_ic")
public class OrderIcEntity {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "订单id")
    private String tbOrderId;

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

    @ApiModelProperty(value = "安装地址")
    private String address;

    @ApiModelProperty(value = "安装开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime installationBeginTime;

    @ApiModelProperty(value = "安装结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected LocalDateTime installationEndTime;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "安装区域id")
    private String areaId;

    @ApiModelProperty(value = "安装人员id")
    private String personId;

    @ApiModelProperty(value = "安装人员name")
    @TableField(exist = false)
    private String personName;

    @ApiModelProperty(value = "安装人员电话")
    @TableField(exist = false)
    private String personMobile;

    @ApiModelProperty(value = "签名图片id")
    private String signatureUrlId;

    @ApiModelProperty(value = "安装多图id")
    private String installUrlIds;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "签到状态")
    private String locationStatus;

    @ApiModelProperty(value = "签到地址")
    private String locationAddressId;

    @ApiModelProperty(value = "签到时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime locationTime;


}
