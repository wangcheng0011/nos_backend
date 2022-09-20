package com.knd.batch.service.feignInterface.fallbackFactory;

import com.knd.batch.service.feignInterface.PayFeignClient;
import com.knd.common.response.CustomResultException;
import com.knd.common.response.ResultEnum;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;


@Component
public class PayFeignClientFallback implements FallbackFactory<PayFeignClient> {
    @Override
    public PayFeignClient create(Throwable throwable) {
        return new PayFeignClient() {
            @Override
            public void closeOvertimeOrderBatch() {
                throw new CustomResultException(ResultEnum.SERVICE_FUSE);
            }
        };
    }
}
