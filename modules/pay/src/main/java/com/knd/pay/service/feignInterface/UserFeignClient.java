package com.knd.pay.service.feignInterface;

import com.knd.common.response.Result;
import com.knd.pay.request.ChangeVipTypeRequest;
import com.knd.pay.service.feignInterface.fallbackFactory.UserFeignClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Component
@FeignClient(value = "front",fallbackFactory = UserFeignClientFallback.class)
public interface UserFeignClient {

    @PostMapping("/front/login/changeVipType")
    Result changeVipType(@Valid @RequestBody ChangeVipTypeRequest changeVipTypeRequest);
}
