package com.knd.manage.course.service.impl;

import com.knd.manage.course.entity.TrainCourseHeadInfo;
import com.knd.manage.course.mapper.TrainCourseHeadInfoMapper;
import com.knd.manage.course.service.ITrainCourseHeadInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-07
 */
@Service
public class TrainCourseHeadInfoServiceImpl extends ServiceImpl<TrainCourseHeadInfoMapper, TrainCourseHeadInfo> implements ITrainCourseHeadInfoService {

    @Override
    public TrainCourseHeadInfo insertReturnEntity(TrainCourseHeadInfo entity) {
        return null;
    }

    @Override
    public TrainCourseHeadInfo updateReturnEntity(TrainCourseHeadInfo entity) {
        return null;
    }
}
