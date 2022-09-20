package com.knd.front.train.service;

import com.knd.common.response.Result;
import com.knd.front.entity.BaseBodyPart;
import com.knd.front.train.request.FilterCourseListRequest;
import com.knd.front.train.request.FilterFreeTrainListRequest;
import com.knd.front.train.request.TrainCourseInfoRequest;
import com.knd.mybatis.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llx
 * @since 2020-07-02
 */
public interface IBaseBodyPartService extends SuperService<BaseBodyPart> {
    Result getFilterCourseList(FilterCourseListRequest filterCourseListRequest);
    Result getFilterFreeTrainLabelSettings(String userId);
    Result getFilterFreeTrainList(FilterFreeTrainListRequest filterFreeTrainListRequest);
    Result commitTrainCourseInfo(TrainCourseInfoRequest trainCourseInfoRequest);
}
