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
     * @param timeId
     * @return
     */
    @PostMapping("/front/live/course/coachTimeCloseRoom")
    Result coachTimeCloseRoom(@RequestParam("timeId") String timeId);

    /**
     * 关闭私教
     * @param courseId
     * @return
     */
    @PostMapping("/front/live/coachOrder/userCoachCloseRoom")
    Result userCoachCloseRoom(@RequestParam("courseId") String courseId);

    /**
     * 移出小组
     * @param id
     * @return
     */
    @PostMapping("/front/live/trainGroup/kickOutGroupForManage")
    Result kickOutGroupForManage(@RequestParam("groupId") String id);


}
