package com.knd.batch.service.impl;

import com.knd.batch.entity.TrainCourseHeadInfo;
import com.knd.batch.mapper.TrainCourseHeadInfoMapper;
import com.knd.batch.service.ITrainCourseHeadInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-08-13
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
