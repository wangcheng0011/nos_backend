package com.knd.manage.course.service;

import com.knd.manage.course.entity.CourseTarget;
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
public interface ICourseTargetService extends SuperService<CourseTarget> {
    //存储课程与目标关联的信息
    void add(CourseTarget courseTarget);

    //根据课程id查询与目标关联的数据id
    List<CourseTarget> getIDListByCourseid(String courseId);

    //删除
    void delete(CourseTarget courseTarget);
}
