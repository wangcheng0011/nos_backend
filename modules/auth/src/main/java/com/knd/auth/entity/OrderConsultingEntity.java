package com.knd.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_order_consulting")
@ApiModel(value="BaseSportTypeEntity对象", description="")
public class OrderConsultingEntity extends BaseEntity{


    @ApiModelProperty(value = "设备安装咨询Id")
    private String id;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "安装地址")
    private String installAddress;

    @ApiModelProperty(value = "详细地址")
    private String detailAddress;

    @ApiModelProperty(value = "购买平台")
    private String buyPlatform;

    @ApiModelProperty(value = "产品型号")
    private String productModel;

    @ApiModelProperty(value = "是否有电梯 0-没有 1-有")
    private String isElevator;

    @ApiModelProperty(value = "安装楼层")
    private String installFloor;

    @ApiModelProperty(value = "安装时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime installStartTime;

    @ApiModelProperty(value = "安装时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime installEndTime;

    @ApiModelProperty(value = "安装位置")
    private String installPosition;

    @ApiModelProperty(value = "安装方式")
    private String installStyle;

    @ApiModelProperty(value = "墙面类型")
    private String wallType;

    @ApiModelProperty(value = "墙体结构")
    private String wallStructure;

    @ApiModelProperty(value = "布线要求")
    private String wiringRequire;

    @ApiModelProperty(value = "安装区域距离插座（米）")
    private String distance;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "安装位置照片")
    private String picAttachId;




}
