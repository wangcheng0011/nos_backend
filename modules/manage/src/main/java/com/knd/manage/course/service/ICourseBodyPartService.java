package com.knd.manage.course.service;

import com.knd.manage.course.entity.CourseBodyPart;
import com.knd.mybatis.SuperService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
public interface ICourseBodyPartService extends SuperService<CourseBodyPart> {
    //存储课程与部位关联的信息
    void add(CourseBodyPart courseBodyPart);
    //根据课程id查询与部位关联的数据id
    List<CourseBodyPart> getIDListByCourseid(String courseId);
    //删除
    void delete(CourseBodyPart courseBodyPart);

}
