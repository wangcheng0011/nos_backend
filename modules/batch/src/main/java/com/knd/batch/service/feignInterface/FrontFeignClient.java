package com.knd.batch.service.feignInterface;

import com.knd.batch.service.feignInterface.fallbackFactory.FrontFeignClientFallback;
import com.knd.common.response.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
@FeignClient(value = "front",fallbackFactory = FrontFeignClientFallback.class)
public interface FrontFeignClient {

    /**
     * 上午下午8点推送
     */
    @RequestMapping(value = "/front/train/trainProgramPush", method = {RequestMethod.POST})
    @ResponseBody
    Result trainProgramPush();


}
