package com.knd.manage.mall.service.feignInterface.fallbackFactory;

import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.mall.dto.CodeDto;
import com.knd.manage.mall.request.GetOrderInfoRequest;
import com.knd.manage.mall.service.feignInterface.FrontInfoFeignClient;
import com.knd.pay.request.ParseOrderNotifyRequest;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;


/**
 * @author will
 */
@Component
public class FrontFeignClientFallback implements FallbackFactory<FrontInfoFeignClient> {
    @Override
    public FrontInfoFeignClient create(Throwable throwable) {
        return new FrontInfoFeignClient() {
            @Override
            public Result sendReceivingGoodsConfirmSms(@RequestBody @Valid CodeDto codeDto) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

            @Override
            public Result getPayInfo(@RequestParam(required = false, name = "response") HttpServletResponse response, @RequestBody @Valid GetOrderInfoRequest request) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

            @Override
            public Result tradeQuery(@RequestParam(required = false, name = "outTradeNo") String outTradeNo, @RequestParam(required = true, name = "tradeNo")  String tradeNo) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

            @Override
            public Result createOfficialAccountUnifiedOrder(@RequestParam(required = false, name = "openid") String openid,
                                                            @RequestParam(required = false, name = "orderNo") String orderNo,
                                                            @RequestParam(required = false, name = "amount") BigDecimal amount) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }


            @Override
            public Result parseOrderNotifyResult(ParseOrderNotifyRequest parseOrderNotifyRequest) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }


            @Override
            public Result alipayCallback(@RequestParam(required = false, name = "outBizNo") String outBizNo,
                                         @RequestParam(required = false, name = "orderId") String orderId) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

        };
    }
}
