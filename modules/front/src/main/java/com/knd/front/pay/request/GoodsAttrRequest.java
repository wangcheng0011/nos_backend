package com.knd.front.pay.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author will
 */
@Data
public class GoodsAttrRequest {
    @NotBlank(message = "属性Id")
    private String id;
    @NotBlank(message = "属性名称")
    private String attrName;
    @NotBlank(message = "属性值")
    private String attrValue;
}
