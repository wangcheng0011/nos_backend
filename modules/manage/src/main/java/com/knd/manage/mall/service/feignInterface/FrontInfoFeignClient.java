package com.knd.manage.mall.service.feignInterface;

import com.knd.common.response.Result;
import com.knd.manage.mall.dto.CodeDto;
import com.knd.manage.mall.request.GetOrderInfoRequest;
import com.knd.manage.mall.service.feignInterface.fallbackFactory.FrontFeignClientFallback;
import com.knd.pay.request.ParseOrderNotifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigDecimal;


/**
 * @author will
 */
@FeignClient(name = "front",fallbackFactory = FrontFeignClientFallback.class)
public interface FrontInfoFeignClient {

    @PostMapping("/front/common/getVerificationCode")
    Result sendReceivingGoodsConfirmSms(@RequestBody @Valid CodeDto codeDto);


    @RequestMapping(value = "/front/login/getPayInfo", method = {RequestMethod.GET})
    @ResponseBody
    Result getPayInfo(@RequestParam(required = false, name = "response") HttpServletResponse response, @RequestBody @Valid GetOrderInfoRequest request);


    @RequestMapping(value = "/front/login/tradeQuery", method = {RequestMethod.GET})
    @ResponseBody
    Result tradeQuery(@RequestParam(required = false, name = "outTradeNo") String outTradeNo,@RequestParam(required = false, name = "tradeNo")  String tradeNo);

    @RequestMapping(value = "/front/login/createOfficialAccountUnifiedOrder", method = {RequestMethod.GET})
    @ResponseBody
    Result createOfficialAccountUnifiedOrder(@RequestParam(required = false, name = "openid") String openid,
                                             @RequestParam(required = false, name = "orderNo") String orderNo,
                                             @RequestParam(required = false, name = "amount") BigDecimal amount);

    @RequestMapping(value = "/front/login/parseOrderNotifyResult", method = {RequestMethod.GET})
    @ResponseBody
    Result parseOrderNotifyResult(ParseOrderNotifyRequest parseOrderNotifyRequest);

    @RequestMapping(value = "/front/login/alipayCallback", method = {RequestMethod.GET})
    @ResponseBody
    Result alipayCallback(@RequestParam(required = false, name = "outBizNo") String outBizNo,
                          @RequestParam(required = false, name = "orderId") String orderId);



}
