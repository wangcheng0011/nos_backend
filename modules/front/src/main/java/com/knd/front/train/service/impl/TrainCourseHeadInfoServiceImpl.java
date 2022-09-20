package com.knd.front.train.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.front.entity.TrainCourseHeadInfo;

import com.knd.front.train.mapper.TrainCourseHeadInfoMapper;
import com.knd.front.train.service.ITrainCourseHeadInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-07-03
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
