package com.knd.front.user.service;

import com.knd.common.response.Result;
import com.knd.front.entity.User;
import com.knd.front.user.dto.UserConsecutiveTrainDayDto;
import com.knd.front.user.dto.UserTrainDataDto;
import com.knd.front.user.request.UserTrainDataQueryRequest;
import com.knd.front.user.request.UserTrainInfoQueryRequest;
import com.knd.mybatis.SuperService;

import java.util.List;

/**
 * @author liulongxiang
 * @interfaceName
 * @description
 * @date 10:25
 * @Version 1.0
 */
public interface IUserInfoService extends SuperService<User> {
    Result getUserCenterInfo(String userId);
    Result getUserTrainCourseDetail(String userId,String trainReportId);
    Result getUserTrainFreeDetail(String userId, String trainReportId);
    Result getUserCourseInfo(String userId, String currentPage);
    Result getUserTrainInfo(String userId,String currentPage);

    UserTrainDataDto getUserTrainData(UserTrainDataQueryRequest userTrainDataQueryRequest);

    List<UserConsecutiveTrainDayDto> getUserTrainConsecutiveDays(UserTrainDataQueryRequest userTrainDataQueryRequest);

    Result getUserTrainInfoNew(UserTrainInfoQueryRequest userTrainInfoQueryRequest);

    Result getUserTrainCourseList(String userId, String currentPage);
}
