package com.knd.front.login.service.feignInterface.fallbackFactory;

import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.front.login.request.CreateOrderRequest;
import com.knd.front.login.service.feignInterface.PayFeignClient;
import com.knd.front.pay.request.ParseOrderNotifyRequest;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;


@Component
public class PayFeignClientFallback implements FallbackFactory<PayFeignClient> {
    @Override
    public PayFeignClient create(Throwable throwable) {
        return new PayFeignClient() {
            @Override
            public Result tradePreCreatePay(@Valid CreateOrderRequest createOrderRequest) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

            @Override
            public Result H5wapPay(@Valid CreateOrderRequest createOrderRequest) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

            @Override
            public Result tradeAppPay(@Valid CreateOrderRequest createOrderRequest) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

            @Override
            public Result jsApiPay(@RequestParam(required = false, name = "response") HttpServletResponse response, @Valid CreateOrderRequest createOrderRequest) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

            @Override
            public Result smallRoutinePay( @Valid CreateOrderRequest createOrderRequest) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

            @Override
            public Result parseOrderNotifyResult( @Valid ParseOrderNotifyRequest parseOrderNotifyRequest) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

            @Override
            public Result tradeQuery(String outTradeNo, String tradeNo) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

            @Override
            public Result tradeClose(String outTradeNo, String tradeNo) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }
            @Override
            public Result tradeRefund(@RequestParam(required = false, name = "outTradeNo") String outTradeNo, @RequestParam(required = false, name = "tradeNo") String tradeNo) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

            @Override
            public Result wxRefundQuery(String transactionId, String outTradeNo, String outRefundNo, String refundId) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

            @Override
            public Result createOfficialAccountUnifiedOrder(@RequestParam(required = false, name = "openid") String openid,
                                                            @RequestParam(required = false, name = "orderNo") String orderNo,
                                                            @RequestParam(required = false, name = "amount") BigDecimal amount) {
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
