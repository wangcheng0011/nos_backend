package com.knd.manage.mall.service.feignInterface;

import com.knd.common.response.Result;
import com.knd.manage.mall.request.CancelOrderCoachRequest;
import com.knd.manage.mall.request.OrderCoachRequest;
import com.knd.manage.mall.service.feignInterface.fallbackFactory.CoachFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author zm
 */
@FeignClient(value = "front",fallbackFactory = CoachFeignClientFallback.class)
public interface CoachFeignClient {

    @PostMapping("/front/live/cancelOrderSuccess")
    Result cancelOrderSuccess(@RequestBody @Valid CancelOrderCoachRequest request);

}
