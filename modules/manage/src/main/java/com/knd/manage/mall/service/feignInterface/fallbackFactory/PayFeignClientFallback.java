package com.knd.manage.mall.service.feignInterface.fallbackFactory;

import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.mall.service.feignInterface.PayFeignClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;


@Component
public class PayFeignClientFallback implements FallbackFactory<PayFeignClient> {
    @Override
    public PayFeignClient create(Throwable throwable) {
        return new PayFeignClient() {
            @Override
            public Result tradeRefund(@RequestParam(required = false, name = "outTradeNo") String outTradeNo, @RequestParam(required = false, name = "tradeNo") String tradeNo) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

            @Override
            public Result wxRefundQuery(String transactionId, String outTradeNo, String outRefundNo, String refundId) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }


        };
    }
}
