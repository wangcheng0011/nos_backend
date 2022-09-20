package com.knd.pay.service.feignInterface;

import com.knd.common.response.Result;
import com.knd.pay.request.CancelOrderCoachRequest;
import com.knd.pay.request.ChangeVipTypeRequest;
import com.knd.pay.request.OrderCoachRequest;
import com.knd.pay.service.feignInterface.fallbackFactory.CoachOrderFeignClientFallback;
import com.knd.pay.service.feignInterface.fallbackFactory.UserFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Component
@FeignClient(value = "front",fallbackFactory = CoachOrderFeignClientFallback.class)
public interface CoachOrderFeignClient {

    @PostMapping("/front/live/orderSuccess")
    Result orderSuccess(@Valid @RequestBody OrderCoachRequest request);

    @PostMapping("/front/live/cancelOrderSuccess")
    Result cancelOrderSuccess(@RequestBody @Valid CancelOrderCoachRequest request);
}
