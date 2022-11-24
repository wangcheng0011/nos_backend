package com.knd.front.login.service.feignInterface;

import com.knd.common.response.Result;
import com.knd.front.login.request.CreateOrderRequest;
import com.knd.front.login.service.feignInterface.fallbackFactory.PayFeignClientFallback;
import com.knd.front.pay.request.ParseOrderNotifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;


@FeignClient(value = "pay",fallbackFactory = PayFeignClientFallback.class)
public interface PayFeignClient {

    @PostMapping("/pay/tradePreCreatePay")
    Result tradePreCreatePay(@Valid @RequestBody CreateOrderRequest createOrderRequest);


    @PostMapping("/pay/H5wapPay")
    Result H5wapPay(@Valid @RequestBody CreateOrderRequest createOrderRequest);

    @PostMapping("/pay/tradeAppPay")
    Result tradeAppPay(@Valid @RequestBody CreateOrderRequest createOrderRequest);

    @PostMapping("/pay/jsApiPay")
    Result jsApiPay(@RequestParam(required = false, name = "response") HttpServletResponse response, @Valid @RequestBody CreateOrderRequest createOrderRequest);


    @PostMapping("/pay/smallRoutinePay")
    Result smallRoutinePay(@Valid @RequestBody CreateOrderRequest createOrderRequest);


    @PostMapping("/pay/parseOrderNotifyResult")
    Result parseOrderNotifyResult(@Valid @RequestBody ParseOrderNotifyRequest parseOrderNotifyRequest);

    @GetMapping(value = "/pay/tradeQuery")
    @ResponseBody
    Result tradeQuery(@RequestParam(required = false, name = "outTradeNo") String outTradeNo, @RequestParam(required = false, name = "tradeNo") String tradeNo);

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/tradeClose")
    @ResponseBody
    Result tradeClose(@RequestParam("outTradeNo") String outTradeNo, @RequestParam("tradeNo") String tradeNo);

    @PostMapping("/pay/tradeRefund")
    Result tradeRefund(@RequestParam(required = false, name = "outTradeNo") String outTradeNo, @RequestParam(required = false, name = "tradeNo") String tradeNo);

    @RequestMapping(value = "/pay/wxRefundQuery", method = {RequestMethod.GET})
    @ResponseBody
    Result wxRefundQuery(@RequestParam("transactionId") String transactionId,
                         @RequestParam("out_trade_no") String outTradeNo,
                         @RequestParam("out_refund_no") String outRefundNo,
                         @RequestParam("refund_id") String refundId);

    @RequestMapping(value = "/pay/createOfficialAccountUnifiedOrder", method = {RequestMethod.GET})
    @ResponseBody
    Result createOfficialAccountUnifiedOrder(@RequestParam(required = false, name = "openid") String openid,
                                             @RequestParam(required = false, name = "orderNo") String orderNo,
                                             @RequestParam(required = false, name = "amount") BigDecimal amount);

    @RequestMapping(value = "/pay/alipayCallback", method = {RequestMethod.GET})
    @ResponseBody
    Result alipayCallback(@RequestParam(required = false, name = "outBizNo") String outBizNo,
                                             @RequestParam(required = false, name = "orderId") String orderId);

}
