package com.knd.front.home.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.front.entity.CourseType;

import com.knd.front.home.mapper.CourseTypeMapper;
import com.knd.front.home.service.ICourseTypeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-07-01
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
}
