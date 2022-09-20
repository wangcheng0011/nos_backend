package com.knd.manage.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.manage.course.entity.CourseType;
import com.knd.manage.course.mapper.CourseTypeMapper;
import com.knd.manage.course.service.ICourseTypeService;
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
public class CourseTypeServiceImpl extends ServiceImpl<CourseTypeMapper, CourseType> implements ICourseTypeService {

    @Override
    public CourseType insertReturnEntity(CourseType entity) {
        return null;
    }

    @Override
    public CourseType updateReturnEntity(CourseType entity) {
        return null;
    }

    @Override
    public void add(CourseType courseType) {
        baseMapper.insert(courseType);
    }

    //根据课程id查询与分类关联的数据id
    @Override
    public List<CourseType> getIDListByCourseid(String courseId) {
        QueryWrapper<CourseType> qw = new QueryWrapper<>();
        qw.eq("courseId",courseId);
        qw.eq("deleted", "0");
        qw.select("id","courseTypeId");
        return baseMapper.selectList(qw);
    }

    //删除
    @Override
    public void delete(CourseType courseType) {
        baseMapper.updateById(courseType);
    }

    //根据课程id查询分类名称
    @Override
    public List<String> getNameListByCourseid(String courseId) {
        return baseMapper.selectNameListByCourseid(courseId);
    }
}
