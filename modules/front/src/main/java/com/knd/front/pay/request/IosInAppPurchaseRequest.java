package com.knd.front.pay.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author will
 */
@Data
@ApiModel("IOS内购会员验证请求对象")
public class IosInAppPurchaseRequest {

    @ApiModelProperty(value = "商品id")
    @NotBlank(message = "商品id不能为空")
    private String goodsId;

    @NotBlank(message = "购买凭证不能为空")
    @ApiModelProperty(value = "购买凭证")
    private String receipt;

    @ApiModelProperty(value = "内购模式：0沙盒测试,1正式")
    @NotBlank(message = "内购模式不能为空")
    @Pattern(regexp = "^(0|1)$",message = "内购模式只能是0沙盒测试,1正式")
    private String verifyType;

}