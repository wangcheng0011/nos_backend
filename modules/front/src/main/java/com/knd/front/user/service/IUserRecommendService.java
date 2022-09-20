package com.knd.front.user.service;

import com.knd.common.response.Result;
import com.knd.front.train.domain.ProgramTypeEnum;

/**
 * @author Lenovo
 */
public interface IUserRecommendService {

    /**
     * 推荐课程
     * @return
     */
    Result getUserRecommendCourse(String userId, String currentPage,String pageSize);

    /**
     * 推荐训练计划列表
     * @return
     */
    Result getUserRecommendTrain(String userId, String currentPage);

    /**
     * 各类型推荐训练计划列表
     */
    Result getTypeTrain(ProgramTypeEnum type, String currentPage);

    /**
     * 推荐训练计划明细
     * @return
     */
    Result getUserRecommendTrainDetail(String userId,String id);

    /**
     * 推荐系列课程列表
     * @return
     */
    Result getUserSeriesCourseList(String userId,String currentPage);

    /**
     * 推荐系列课程明细
     * @return
     */
    Result getUserSeriesCourseDetail(String userId,String id);


    Result getUserRecommendCourseAndHotCourse(String userId, String courseType, String currentPage, String pageSize);
}
