package com.knd.manage.mall.service.feignInterface;

import com.knd.common.response.Result;
import com.knd.manage.mall.service.feignInterface.fallbackFactory.PayFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "pay",fallbackFactory = PayFeignClientFallback.class)
public interface PayFeignClient {

    @PostMapping("/pay/tradeRefund")
    Result tradeRefund(@RequestParam(required = false, name = "outTradeNo") String outTradeNo, @RequestParam(required = false, name = "tradeNo") String tradeNo);

    @RequestMapping(value = "/pay/wxRefundQuery", method = {RequestMethod.GET})
    @ResponseBody
    Result wxRefundQuery(@RequestParam("transactionId") String transactionId,
                                @RequestParam("out_trade_no") String outTradeNo,
                                @RequestParam("out_refund_no") String outRefundNo,
                                @RequestParam("refund_id") String refundId);


}
