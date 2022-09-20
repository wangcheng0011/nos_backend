package com.knd.front.train.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.front.entity.TrainCourseActInfo;

import com.knd.front.train.mapper.TrainCourseActInfoMapper;
import com.knd.front.train.service.ITrainCourseActInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-07-07
 */
@Service
public class TrainCourseActInfoServiceImpl extends ServiceImpl<TrainCourseActInfoMapper, TrainCourseActInfo> implements ITrainCourseActInfoService {

    @Override
    public TrainCourseActInfo insertReturnEntity(TrainCourseActInfo entity) {
        return null;
    }

    @Override
    public TrainCourseActInfo updateReturnEntity(TrainCourseActInfo entity) {
        return null;
    }
}
