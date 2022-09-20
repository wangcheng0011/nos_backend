//package com.knd.manage.user.service.feignInterface.fallbackFactory;
//
//import com.knd.common.response.Result;
//import com.knd.common.response.ResultEnum;
//import com.knd.common.response.ResultUtil;
//import com.knd.manage.user.service.feignInterface.JobService;
//import feign.hystrix.FallbackFactory;
//import org.springframework.stereotype.Component;
//
//import javax.validation.Valid;
//
//
//@Component
//public class JobFallback implements FallbackFactory<JobService> {
//    @Override
//    public JobService create(Throwable throwable) {
//        return new JobService() {
//            @Override
//            public Result jobList() {
//                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
//            }
//
//            @Override
//            public Result runAJobNow2() {
//                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
//            }
//        };
//    }
//}
