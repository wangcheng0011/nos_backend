package com.knd.manage.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.manage.course.entity.CourseBodyPart;
import com.knd.manage.course.mapper.CourseBodyPartMapper;
import com.knd.manage.course.service.ICourseBodyPartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-02
 */
@Service
public class CourseBodyPartServiceImpl extends ServiceImpl<CourseBodyPartMapper, CourseBodyPart> implements ICourseBodyPartService {

    @Override
    public CourseBodyPart insertReturnEntity(CourseBodyPart entity) {
        return null;
    }

    @Override
    public CourseBodyPart updateReturnEntity(CourseBodyPart entity) {
        return null;
    }

    //存储课程与部位关联的信息
    @Override
    public void add(CourseBodyPart courseBodyPart) {
        baseMapper.insert(courseBodyPart);
    }

    @Override
    public List<CourseBodyPart> getIDListByCourseid(String courseId) {
        QueryWrapper<CourseBodyPart> qw = new QueryWrapper<>();
        qw.eq("courseId",courseId);
        qw.eq("deleted", "0");
        qw.select("id","partId");
        return baseMapper.selectList(qw);
    }

    @Override
    public void delete(CourseBodyPart courseBodyPart) {
        baseMapper.updateById(courseBodyPart);
    }
}
