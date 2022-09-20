//package com.knd.manage.user.service.feignInterface;
//
//import com.knd.common.response.Result;
//import com.knd.common.response.ResultUtil;
//import com.knd.manage.user.service.feignInterface.fallbackFactory.GetUserDetailServiceFallback;
//import com.knd.manage.user.service.feignInterface.fallbackFactory.JobFallback;
//import io.swagger.annotations.ApiOperation;
//import lombok.NonNull;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//
//@FeignClient(name = "BATCH", fallbackFactory = JobFallback.class)
//public interface JobService {
//    //第一二级路径
//    String prefix = "/job";
//
//    //    @ApiOperation("获取任务列表")
//    @RequestMapping(value = prefix + "/jobList", method = RequestMethod.GET)
//    public Result jobList();
//
//    //    @ApiOperation("立即执行任务")
//    @RequestMapping(value = prefix + "/runAJobNow", method = RequestMethod.POST)
//    public Result runAJobNow2();
//
//}
