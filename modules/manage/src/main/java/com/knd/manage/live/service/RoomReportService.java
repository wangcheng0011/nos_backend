package com.knd.manage.live.service;

import com.knd.common.response.Result;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.live.request.ManageRequest;
import com.knd.manage.live.request.RoomReportListRequest;

/**
 * 直播房间举报管理
 */
public interface RoomReportService {

    //获取房间举报列表
    Result getRoomReportList(RoomReportListRequest vo);

    //获取房间举报详情
    Result getRoomReport(String id);

    //1冻结用户，2关闭房间，3踢出小组
    Result operation(ManageRequest vo);

    //删除举报
    Result delete(VoId vo);

    //关闭房间
    Result closeRoom(String id);

    //关闭直播
    Result closeUserCoachTime(String id);

    //关闭私教
    Result closeUserCoachCourseOrder(String id);
}
