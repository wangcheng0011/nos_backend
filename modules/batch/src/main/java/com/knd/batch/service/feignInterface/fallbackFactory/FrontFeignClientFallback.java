package com.knd.batch.service.feignInterface.fallbackFactory;

import com.knd.batch.service.feignInterface.FrontFeignClient;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;


@Component
public class FrontFeignClientFallback implements FallbackFactory<FrontFeignClient> {
    @Override
    public FrontFeignClient create(Throwable throwable) {
        return new FrontFeignClient() {
            @Override
            public Result trainProgramPush() {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }
        };
    }
}
