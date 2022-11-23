package com.knd.front.live.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 用户预约记录表
 * @author zm
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lb_user_order_record")
@ApiModel(value="UserOrderRecordEntity对象", description="")
public class UserOrderRecordEntity extends BaseEntity {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "是否已读0否 1是")
    private String isRead;

    @ApiModelProperty(value = "预约类别0课前咨询 1私教课程 2团课直播 3小组 4预约计划 5计划提醒")
    private String orderType;

    @ApiModelProperty(value = "预约名称")
    private String orderName;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "预约时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime orderTime;

    @ApiModelProperty(value = "关联id")
    private String relevancyId;

}
