package com.knd.front.home.service;

import com.knd.common.response.Result;
import com.knd.front.entity.IntroductionCourse;
import com.knd.front.home.request.FinishWatchCourseVideoRequest;
import com.knd.front.home.request.WatchCourseVideoRequest;
import com.knd.mybatis.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llx
 * @since 2020-07-02
 */
public interface IIntroductionCourseService extends SuperService<IntroductionCourse> {
    Result watchCourseVideo(WatchCourseVideoRequest watchCourseVideoRequest);
    Result finishWatchCourseVideo(FinishWatchCourseVideoRequest finishWatchCourseVideoRequest);
}
