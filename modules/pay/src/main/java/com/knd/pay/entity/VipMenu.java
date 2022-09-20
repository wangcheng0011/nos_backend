package com.knd.pay.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.knd.mybatis.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 会员套餐表 
 * </p>
 *
 * @author will
 * @since 2021-01-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vip_menu")
@ApiModel(value="VipMenu对象", description="会员套餐表 ")
public class VipMenu extends BaseEntity {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "会员套餐名称 0普通会员1个人会员2家庭主会员3家庭副会员")
    private String vipName;

    @ApiModelProperty(value = "会员套餐名称 0普通会员1个人会员2家庭主会员3家庭副会员")
    private String vipType;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "0包年1季度2包月")
    private String packageType;

    @ApiModelProperty(value = "原价")
    private BigDecimal costPrice;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "排序")
    private String sort;


}
