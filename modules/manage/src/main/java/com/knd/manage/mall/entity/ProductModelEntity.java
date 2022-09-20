package com.knd.manage.mall.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("base_product_model")
public class ProductModelEntity {

    private String id;

    @ApiModelProperty(value = "产品型号")
    private String productModel;
}
