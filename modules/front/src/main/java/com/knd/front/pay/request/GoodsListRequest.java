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
public class GoodsListRequest {

    @ApiModelProperty(value = "分类集合1会员充值 2配件 3付费课程 4机器")
    private List<String> typeList;

    @ApiModelProperty(value = "第几页")
    @NotBlank
    private String current;

    // 2021/9/1 搜索接口
    private String goodName;

}
