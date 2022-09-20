package com.knd.manage.mall.request;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


/**
 * @author will
 */
@Data
public class TbOrderItemRequest {

    @ApiModelProperty(value = "购买数量")
    private String quantity;

    @ApiModelProperty(value = "购买商品Id")
    private String goodsId;

}
