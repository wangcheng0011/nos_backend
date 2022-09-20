package com.knd.manage.course.service.feignInterface.fallbackFactory;

import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.course.request.SaveTrainProgramRequest;
import com.knd.manage.course.service.feignInterface.GetTrainProgramFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


@Component
public class GetTrainProgramFeignFallback implements FallbackFactory<GetTrainProgramFeign> {
    @Override
    public GetTrainProgramFeign create(Throwable throwable) {
        return new GetTrainProgramFeign() {
            @Override
            public Result saveTrainProgram(@RequestBody @Valid SaveTrainProgramRequest saveTrainProgramRequest) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }
        };
    }
}
