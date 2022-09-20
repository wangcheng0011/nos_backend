package com.knd.batch.service.feignInterface;

import com.knd.batch.service.feignInterface.fallbackFactory.PayFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
@FeignClient(value = "pay",fallbackFactory = PayFeignClientFallback.class)
public interface PayFeignClient {

    /**
     * 批处理关闭超时订单
     */
    @GetMapping(value = "/pay/closeOvertimeOrderBatch")
    @ResponseBody
    void closeOvertimeOrderBatch();


}
