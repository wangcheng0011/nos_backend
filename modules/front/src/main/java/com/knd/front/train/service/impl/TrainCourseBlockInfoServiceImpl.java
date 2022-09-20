package com.knd.front.train.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.front.entity.TrainCourseBlockInfo;

import com.knd.front.train.mapper.TrainCourseBlockInfoMapper;
import com.knd.front.train.service.ITrainCourseBlockInfoService;
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
public class TrainCourseBlockInfoServiceImpl extends ServiceImpl<TrainCourseBlockInfoMapper, TrainCourseBlockInfo> implements ITrainCourseBlockInfoService {

    @Override
    public TrainCourseBlockInfo insertReturnEntity(TrainCourseBlockInfo entity) {
        return null;
    }

    @Override
    public TrainCourseBlockInfo updateReturnEntity(TrainCourseBlockInfo entity) {
        return null;
    }
}
