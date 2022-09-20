package com.knd.manage.mall.service.feignInterface.fallbackFactory;

import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.mall.request.CancelOrderCoachRequest;
import com.knd.manage.mall.request.OrderCoachRequest;
import com.knd.manage.mall.service.feignInterface.CoachFeignClient;
import com.knd.manage.mall.service.feignInterface.PayFeignClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;


@Component
public class CoachFeignClientFallback implements FallbackFactory<CoachFeignClient> {
    @Override
    public CoachFeignClient create(Throwable throwable) {
        return new CoachFeignClient() {
            @Override
            public Result cancelOrderSuccess(@Valid CancelOrderCoachRequest request) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }
        };
    }
}
