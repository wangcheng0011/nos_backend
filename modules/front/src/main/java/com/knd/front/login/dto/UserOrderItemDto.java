package com.knd.front.login.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.front.entity.UserReceiveAddressEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/17
 * @Version 1.0
 */
@Data
public class UserOrderItemDto {

    private String id;

    private String goodsName;

    /*
     * 商品介绍
     */
    private String goodsDesc;

    /**
     * 商品图片附件id
     */
    private String coverUrl;

    /**
     * 价格
     */
    private BigDecimal price;

    @ApiModelProperty(value = "购买数量")
    private String quantity;

    @ApiModelProperty(value = "主题描述")
    private String description;


}