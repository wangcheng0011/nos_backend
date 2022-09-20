package com.knd.manage.user.service.feignInterface.fallbackFactory;


import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.user.service.feignInterface.QueryUserTrainInfoService;
import feign.hystrix.FallbackFactory;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Component
public class QueryUserTrainInfoServiceFallback implements FallbackFactory<QueryUserTrainInfoService> {
    @Override
    public QueryUserTrainInfoService create(Throwable throwable) {
        return new QueryUserTrainInfoService() {
            @Override
            public Result getUserTrainCourseDetail(@NotBlank String userId, @NotBlank String trainReportId) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }

            @Override
            public Result getUserTrainFreeDetail(@NonNull String userId, @NonNull String trainReportId) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }
        };
    }
}
