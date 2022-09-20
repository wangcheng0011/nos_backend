package com.knd.manage.mall.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author will
 * @date 2021年04月14日 15:57
 */
@Data
@ApiModel("订单参数对象")
public class OrderQueryParam {
    @ApiModelProperty("检索关键字")
    //订单号、手机号
    private String keyWord;
//    @ApiModelProperty("订单列表类型")
//    @Pattern(regexp = "^(1|2)$", message = "订单列表类型只能是 1->购买单,2->安装单")
//    private String listType;
//    @ApiModelProperty("订单状态")
//    @Pattern(regexp = "^(1|2|3|4|5|6)$", message = "订单状态只能是 1->未付款,2->(已付款)待发货,3->已发货,4->已完成，5->已取消,6->已退款")
//    private String orderStatus;
    @ApiModelProperty("安装状态")
    @Pattern(regexp = "^(0|1|2|3|4)$", message = "安装状态只能是 0->待分配;1->待接受;2->待安装;3->安装中;4->已确认安装")
    private String installStatus;
    @ApiModelProperty("请求页数")
    @NotBlank(message = "请求页数不可为空")
    private String current;
    @ApiModelProperty("每页展示数")
    private String pageSize = "10";
}
