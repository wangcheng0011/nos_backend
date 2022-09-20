package com.knd.manage.user.service.feignInterface;

import com.knd.common.response.Result;
import com.knd.manage.user.service.feignInterface.fallbackFactory.GetUserDetailServiceFallback;
import lombok.NonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "FRONT",fallbackFactory = GetUserDetailServiceFallback.class)
public interface GetUserDetailService {
    //第一二级路径
    String prefix = "/front/login";

    //获取用户运动详情(课程训练）
    @RequestMapping(value = prefix + "/getUserDetail", method = RequestMethod.GET)
    Result getUserDetail(@RequestParam @NonNull String userId);


}
