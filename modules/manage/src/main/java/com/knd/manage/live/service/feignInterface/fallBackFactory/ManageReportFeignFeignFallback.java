package com.knd.manage.live.service.feignInterface.fallBackFactory;

import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.live.service.feignInterface.FrontReportFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;


@Component
public class ManageReportFeignFeignFallback implements FallbackFactory<FrontReportFeign> {
    @Override
    public FrontReportFeign create(Throwable throwable) {
        return new FrontReportFeign() {
            @Override
            public Result closeRoomForManage(@RequestParam("id") String id) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }
            @Override
            public Result closeUserCoachTime(@RequestParam("id") String id) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }
            @Override
            public Result closeUserCoachCourseOrder(@RequestParam("id") String id) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }
            @Override
            public Result kickOutGroupForManage(@RequestParam("groupId") String id) {
                return ResultUtil.error(ResultEnum.SERVICE_FUSE);
            }
        };
    }
}
