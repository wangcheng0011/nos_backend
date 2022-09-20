package com.knd.manage.course.service;

import com.knd.manage.course.entity.CourseType;
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
public interface ICourseTypeService extends SuperService<CourseType> {
    //存储课程与分类关联的信息
    void add(CourseType courseType);
    //根据课程id查询与分类关联的数据id
    List<CourseType> getIDListByCourseid(String courseId);
    //删除
    void delete(CourseType courseType);
    //根据课程id查询与分类名称
     List<String> getNameListByCourseid(String courseId);


}
