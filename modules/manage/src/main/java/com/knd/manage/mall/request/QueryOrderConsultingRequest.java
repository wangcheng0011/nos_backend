package com.knd.manage.mall.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author will
 */
@Data
@ApiModel("设备咨询查询请求")
public class QueryOrderConsultingRequest {

    @ApiModelProperty("检索关键字 用户姓名、电话、安装地址")
    private String keyWord;

    @ApiModelProperty("是否有电梯 0-没有 1-有")
    private String isElevator;

    @ApiModelProperty(value = "安装时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime installStartTime;

    @ApiModelProperty(value = "安装时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime installEndTime;

    @ApiModelProperty("购买平台")
    private String buyPlatform;

    @ApiModelProperty("请求页数")
    private String current;

    @ApiModelProperty("每页展示数")
    private String pageSize = "10";




}