package com.knd.front.live.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.response.Result;
import com.knd.front.live.dto.CoachCourseTimeDto;
import com.knd.front.live.request.CoachRequest;
import com.knd.front.live.request.SaveCoachCourseRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * 教练
 */
public interface CoachService {

    /**
     * 获取全部教练列表
     * @return
     */
    Result getCoachList(CoachRequest request);

    /**
     * 获取教练详情
     * @param userId
     * @return
     */
    Result getCoachDetails(String userId,String coachUserId);

    /**
     * 获取我的私教列表
     * @param userId
     * @return
     */
    Result getCoachByUser(String userId);

    /**
     * 获取已预约教练课程列表
     * @return
     */
    Result getCoachCourseByUserList(String userId,String coachUserId,List<String> typeList,String current);

    /**
     * 获取教练课预约列表（每天）
     * @return
     */
    Result getCoachOrderList(String userId,List<String> typeList, String coachUserId, LocalDate beginDate,LocalDate endDate);

    /**
     * 日历（教练查看课程）
     * @param userId
     * @param beginDate
     * @param endDate
     * @return
     */
    Result getDayOrderList(String userId, LocalDate beginDate,LocalDate endDate);

    /**
     * 根据课程时间id获取课程详情
     * @param timeId
     * @return
     */
    Result getCoachCourseById(String timeId,String userId);

    /**
     * 未来直播/往期直播
     * @param type
     * @param coachUserId
     * @return
     */
    Result getLiveList(String userId,String current,String type,String coachUserId,String courseType);

    /**
     * 正在直播
     * @param current
     * @return
     */
    Result getLiveIngList(String userId,String current);

    Result<Page<CoachCourseTimeDto>> queryCoachCourseTime(String current, String queryType);

    /**
     * 新增教练课程
     * @param userId
     * @param vo
     * @return
     */
    Result publishCourse(String userId, SaveCoachCourseRequest vo);

}
