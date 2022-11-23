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
    Result getRoomToken(String id,String type);

    Result endCourse(String id);

}
