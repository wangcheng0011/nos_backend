package com.knd.manage.user.service.feignInterface.fallbackFactory;

import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.user.service.feignInterface.GetUserDetailService;
import feign.hystrix.FallbackFactory;
import lombok.NonNull;
import org.springframework.stereotype.Component;


@Component
public class GetUserDetailServiceFallback implements FallbackFactory<GetUserDetailService> {
    @Override
    public GetUserDetailService create(Throwable throwable) {
        return new GetUserDetailService() {
            @Override
            public Result getUserDetail(@NonNull String userId) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }
        };
    }
}
