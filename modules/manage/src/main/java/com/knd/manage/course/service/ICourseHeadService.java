package com.knd.manage.course.service;

import com.knd.common.response.Result;
import com.knd.manage.course.entity.CourseHead;
import com.knd.manage.course.vo.CourseSort;
import com.knd.manage.course.vo.VoSaveCourseHead;
import com.knd.manage.course.vo.VoSaveCourseReleaseFlag;
import com.knd.manage.course.vo.VoSaveCourseVedioDuration;
import com.knd.mybatis.SuperService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
public interface ICourseHeadService extends SuperService<CourseHead> {
    //新增课程
    Result add(VoSaveCourseHead voSaveCourseHead);

    //更新课程
    Result edit(VoSaveCourseHead voSaveCourseHead);

    //设置课程发布状态
    Result saveCourseReleaseFlag(VoSaveCourseReleaseFlag vo);

    //删除课程
    Result deleteCourse(String userId, String id);

    //获取课程维护信息
    Result getCourseHead(String id);

    //获取课程列表
    Result getCourseHeadList(String course, String typeid, String targetid, String partid, String releaseFlag,String courseType, String current);

    //批量更新
    Result updateCoursesSort(String userId, List<CourseSort> courseSortList);

    //获取课程视频信息
    Result getCourseVedioInfo(String id);

    //维护视频时长
    Result saveCourseVedioDuration(VoSaveCourseVedioDuration vo);


}
