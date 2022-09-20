package com.knd.front.live.service;

import com.knd.common.response.Result;

/**
 * 直播课程
 */
public interface ILiveCourseService {

    /**
     * 获取直播课程roomToken
     * @return
     */
    Result getRoomToken(String timeId,String type);

    Result endCourse(String id);

    /**
     * 关闭直播接口
     * @return
     */
    Result closeUserCoachTime(String id);
}
