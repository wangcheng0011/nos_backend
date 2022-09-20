package com.knd.front.logistics.entity;

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
 * @author will
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("logistics")
@ApiModel(value="Logistics对象", description="")
public class LogisticsEntity extends BaseEntity {

    @ApiModelProperty(value = "位置")
    private String site;
    @ApiModelProperty(value = "城市")
    private String city;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "快递单号")
    private String trackingNumber;
    @ApiModelProperty(value = "排序")
    private String sort;
    @ApiModelProperty(value = "物流公司")
    private String logisticsCompanies;
    @ApiModelProperty(value = "时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime time;


}
