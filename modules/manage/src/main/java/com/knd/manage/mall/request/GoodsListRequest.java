package com.knd.manage.mall.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
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
