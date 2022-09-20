package com.knd.manage.mall.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author will
 */
@Data
@TableName("tb_order_operate_history")
@ApiModel(value="TbOrderOperateHistoryEntity", description="")
public class TbOrderOperateHistoryEntity {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "操作人id")
    private String operateId;

    @ApiModelProperty(value = "操作人类型")
    private String operateType;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "订单状态：1未付款,2已付款,3已发货,4已完成,5已取消,6已退款,7待接受,8待安装，9安装中，10安装完成")
    private String orderStatus;

    @ApiModelProperty(value = "备注")
    private String note;
}
