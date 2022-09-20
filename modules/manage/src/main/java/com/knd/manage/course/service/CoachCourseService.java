package com.knd.manage.course.service;

import com.knd.common.response.Result;
import com.knd.manage.course.vo.VoGetCoachCourseList;
import com.knd.manage.course.vo.VoSaveCoachCourse;

/**
 * @author zm
 * 教练课程
 */
public interface CoachCourseService {

    //获取教练课程列表
    Result getCoachCourseList(VoGetCoachCourseList vo);

    //获取教练课程
    Result getCoachCourse(String id);

    //新增教练课程
    Result add(VoSaveCoachCourse vo);

    //更新教练课程
    Result edit(VoSaveCoachCourse vo);

    //删除教练课程
    Result delete(String userId, String id);


}
