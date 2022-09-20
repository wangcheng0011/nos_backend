package com.knd.pay.service.feignInterface.fallbackFactory;

import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.pay.request.CancelOrderCoachRequest;
import com.knd.pay.request.ChangeVipTypeRequest;
import com.knd.pay.request.OrderCoachRequest;
import com.knd.pay.service.feignInterface.CoachOrderFeignClient;
import com.knd.pay.service.feignInterface.UserFeignClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import javax.validation.Valid;


@Component
public class CoachOrderFeignClientFallback implements FallbackFactory<CoachOrderFeignClient> {
    @Override
    public CoachOrderFeignClient create(Throwable throwable) {
        return new CoachOrderFeignClient() {
            @Override
            public Result orderSuccess(@Valid OrderCoachRequest request) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

            @Override
            public Result cancelOrderSuccess(@Valid CancelOrderCoachRequest request) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

        };
    }
}
