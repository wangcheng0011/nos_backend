package com.knd.manage.course.mapper;

import com.knd.manage.course.entity.CourseTarget;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
public interface CourseTargetMapper extends BaseMapper<CourseTarget> {

    //根据课程id获取目标名称
    List<String> selectNameListByCourseid(String courseid);
    //检查是否有未删除的课程使用该目标
    int selectCourseCountById(String id);

}
