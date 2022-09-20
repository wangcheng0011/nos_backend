package com.knd.manage.course.service.feignInterface;

import com.knd.common.response.Result;
import com.knd.manage.course.request.SaveTrainProgramRequest;
import com.knd.manage.course.service.feignInterface.fallbackFactory.GetTrainProgramFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


@FeignClient(name = "front",fallbackFactory = GetTrainProgramFeignFallback.class)
public interface GetTrainProgramFeign {

    //保存训练计划
//    @RequestMapping(value = "/front/train/saveTrainProgram", method = RequestMethod.POST)
//    Result saveTrainProgram(@RequestBody SaveTrainProgramRequest saveTrainProgramRequest);

    @PostMapping("/front/train/saveTrainProgram")
    Result saveTrainProgram(@RequestBody @Valid SaveTrainProgramRequest saveTrainProgramRequest);

}
