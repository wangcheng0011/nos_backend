package com.knd.front.pay.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ChangeOrderStatusRequest {
    //userId从token获取
    @ApiModelProperty(value = "会员ID")
    private String userId ;
    @NotBlank

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "订单id")
    private String id;
    @NotBlank
    @Pattern(regexp = "^(1|2|3|4|5|6|7|1000)$")
    @ApiModelProperty(value = "状态：1未付款,2已付款,3已发货,4已完成,5已取消,6退款中,7已退款")
    private String status;

    @ApiModelProperty(value = "快递单号")
    private String trackingNumber;

    @ApiModelProperty("物流公司 1德邦 2安能")
    private String logisticsCompanies;

    @ApiModelProperty(value = "预期送达时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String deliveryDate;
}
