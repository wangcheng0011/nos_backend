package com.knd.manage.mall.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author will
 * @date 2021年04月14日 15:57
 */
@Data
@ApiModel("订单参数对象")
public class OrderListQueryParam {
    @ApiModelProperty("1未付款,2已付款,3已关闭,4已退款,5已取消,6已退款")
    private String status;
    @ApiModelProperty("物流公司 1德邦 2安能")
    private String logisticsCompanies;
    @ApiModelProperty("物流单号")
    private String trackingNumber;
    @ApiModelProperty(value = "创建订单开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime orderCreateStartTime;
    @ApiModelProperty(value = "创建订单结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime orderCreateEndTime;
    @ApiModelProperty("订单号")
    private String orderNo;
    @ApiModelProperty("当前页")
    private String current;

}
