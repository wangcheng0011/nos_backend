package com.knd.manage.live.service.feignInterface;

import com.knd.common.response.Result;
import com.knd.manage.config.FeignConfiguration;
import com.knd.manage.live.service.feignInterface.fallBackFactory.ManageReportFeignFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "front", configuration = FeignConfiguration.class,fallbackFactory = ManageReportFeignFeignFallback.class)
public interface FrontReportFeign {

    /**
     * 关闭房间
     * @param id
     * @return
     */
    @PostMapping("/front/live/trainGroup/closeRoomForManage")
    Result closeRoomForManage(@RequestParam("id") String id);

    /**
     * 关闭直播
     * @param id
     * @return
     */
    @PostMapping("/front/live//course/closeUserCoachTime")
    Result closeUserCoachTime(@RequestParam("id") String id);

    /**
     * 关闭私教
     * @param id
     * @return
     */
    @PostMapping("/front/live/coachOrder/closeUserCoachCourseOrder")
    Result closeUserCoachCourseOrder(@RequestParam("id") String id);

    /**
     * 移出小组
     * @param id
     * @return
     */
    @PostMapping("/front/live/trainGroup/kickOutGroupForManage")
    Result kickOutGroupForManage(@RequestParam("groupId") String id);


}
