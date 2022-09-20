package com.knd.front.pay.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author will
 */
@Data
public class CreateGoodsRequest {

    @Size(max = 64, message = "id最大长度为64")
    @NotBlank(message = "id不可空")
    private String id;
    @NotBlank(message = "动作名称不可空")
    @Size(max = 32, message = "最大长度为32")
    private String goodsName;

    @NotBlank(message = "商品类型 0-虚拟 1-实物")
    @Pattern(regexp = "^(0|1)$", message = "商品类型只能是 0-虚拟 1-实物")
    private String goodsType;
    //非必传
    @Size(max = 256, message = "商品描述最大长度为256")
    private String goodsDesc;
    @NotBlank(message = "分类id不可空")
    @Pattern(regexp = "^(2|4)$", message = "分类id只能是 2-配件 4-机器")
    private String categoryId;

    @ApiModelProperty(value = "商品金额")
    @NotNull
    @DecimalMin(value = "0",message = "商品金额不能小于0")
    private BigDecimal price;

    @Valid
    private List<GoodsAttrRequest> attrList;







}
