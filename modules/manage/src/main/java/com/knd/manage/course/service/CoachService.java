package com.knd.manage.course.service;

import com.knd.common.response.Result;
import com.knd.manage.course.vo.VoSaveCoach;
import com.knd.manage.course.vo.VoGetCoachList;

/**
 * @author zm
 * 教练
 */
public interface CoachService {

    //获取教练列表
    Result getCoachList(VoGetCoachList vo);

    //获取教练信息
    Result getCoach(String id);

    //更新教练信息
    Result edit(VoSaveCoach vo);




}
