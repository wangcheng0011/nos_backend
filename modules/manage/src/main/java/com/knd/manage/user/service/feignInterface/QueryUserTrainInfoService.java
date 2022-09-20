package com.knd.manage.user.service.feignInterface;

import com.knd.common.response.Result;
import com.knd.manage.user.service.feignInterface.fallbackFactory.QueryUserTrainInfoServiceFallback;
import lombok.NonNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;

//注入远程服务的应用名【不区分大小写】 ，以及熔断回调类
@FeignClient(name = "FRONT", fallbackFactory = QueryUserTrainInfoServiceFallback.class)
public interface QueryUserTrainInfoService {
    //第一二级路径
    String prefix = "/front/user";

    //获取用户运动详情(课程训练）
    @RequestMapping(value = prefix + "/getUserTrainCourseDetail", method = RequestMethod.GET)
    Result getUserTrainCourseDetail(@RequestParam @NotBlank String userId, @RequestParam @NotBlank String trainReportId);

    //获取用户运动详情（自由训练)
    @RequestMapping(value = prefix + "/getUserTrainFreeDetail", method = RequestMethod.GET)
    Result getUserTrainFreeDetail(@RequestParam @NonNull String userId, @RequestParam @NonNull String trainReportId);


}
