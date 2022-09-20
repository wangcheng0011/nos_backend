package com.knd.front.live.service;

import com.knd.common.response.Result;
import com.knd.front.live.request.OrderCoachRequest;

import javax.servlet.http.HttpServletResponse;

/**
 * 教练预约模块
 * @author zm
 */
public interface CoachOrderService {

    /**
     * 获取用户已预约教练课程列表
     * @param userId
     * @param current
     * @return
     */
    Result getCoachOrderList(String userId,String current);

    /**
     * 预约接口
     * @param request
     * @param platform:平台
     * @return
     */
    Result orderTime(HttpServletResponse response, OrderCoachRequest request, String platform);

    /**
     * 预约成功回调接口
     * @param request
     * @return
     */
    Result orderSuccess(OrderCoachRequest request);

    /**
     * 取消预约接口
     * @param coachTimeId
     * @param userId
     * @return
     */
    Result cancelOrderTime(String coachTimeId ,String userId);

    /**
     * 取消预约成功回调接口
     * @param userId
     * @param coachTimeId
     * @param userId
     * @return
     */
    Result cancelOrderSuccess(String coachTimeId,String userId);

    /**
     * 关闭私教接口
     * @param id
     * @return
     */
    Result closeUserCoachCourseOrder(String id);
}
