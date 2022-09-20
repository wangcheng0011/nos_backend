package com.knd.manage.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.manage.course.entity.CourseTarget;
import com.knd.manage.course.mapper.CourseTargetMapper;
import com.knd.manage.course.service.ICourseTargetService;
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
public class CourseTargetServiceImpl extends ServiceImpl<CourseTargetMapper, CourseTarget> implements ICourseTargetService {

    @Override
    public CourseTarget insertReturnEntity(CourseTarget entity) {
        return null;
    }

    @Override
    public CourseTarget updateReturnEntity(CourseTarget entity) {
        return null;
    }

    //存储课程与目标关联的信息
    @Override
    public void add(CourseTarget courseTarget) {
        baseMapper.insert(courseTarget);
    }

    @Override
    public List<CourseTarget> getIDListByCourseid(String courseId) {
        QueryWrapper<CourseTarget> qw = new QueryWrapper<>();
        qw.eq("courseId",courseId);
        qw.eq("deleted", "0");
        qw.select("id","targetId");
        return baseMapper.selectList(qw);
    }

    @Override
    public void delete(CourseTarget courseTarget) {
        baseMapper.updateById(courseTarget);
    }
}
