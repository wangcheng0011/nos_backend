package com.knd.pay.service.feignInterface.fallbackFactory;

import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.pay.request.ChangeVipTypeRequest;
import com.knd.pay.service.feignInterface.UserFeignClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import javax.validation.Valid;


@Component
public class UserFeignClientFallback implements FallbackFactory<UserFeignClient> {
    @Override
    public UserFeignClient create(Throwable throwable) {
        return new UserFeignClient() {
            @Override
            public Result changeVipType(@Valid ChangeVipTypeRequest changeVipTypeRequest) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

        };
    }
}
