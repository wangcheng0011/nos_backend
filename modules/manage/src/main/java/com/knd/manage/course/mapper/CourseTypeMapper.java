package com.knd.manage.course.mapper;

import com.knd.manage.course.entity.CourseType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
public interface CourseTypeMapper extends BaseMapper<CourseType> {
    //根据课程id查询关联的分类名称
     List<String> selectNameListByCourseid(String courseId);

    //检查是否有未删除的课程使用该类型
    int selectCourseCountByTypeId(String id);

}
