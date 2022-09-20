package com.knd.front.pay.request;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.front.dto.VoUrl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author will
 */
@Data
@ApiModel("设备咨询表请求")
public class OrderConsultingRequest {

    @ApiModelProperty(value = "id")
    private String id;


    @ApiModelProperty(value = "会员ID")
    private String userId;


    @ApiModelProperty(value = "用户姓名")
    @NotBlank(message = "用户姓名不能为空")
    private String userName;

    @ApiModelProperty(value = "电话")
    @NotBlank(message = "电话不能为空")
    private String phone;

    @ApiModelProperty(value = "安装地址")
    @NotBlank(message = "安装地址不能为空")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime installStartTime;

    @ApiModelProperty(value = "安装时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
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
    private String pictureUrlId;

    @ApiModelProperty(value = "安装位置照片")
    private VoUrl pictureUrl;






}