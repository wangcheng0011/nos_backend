package com.knd.manage.course.service.impl;

import com.knd.manage.course.entity.TrainFreeHead;
import com.knd.manage.course.mapper.TrainFreeHeadMapper;
import com.knd.manage.course.service.ITrainFreeHeadService;
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
public class TrainFreeHeadServiceImpl extends ServiceImpl<TrainFreeHeadMapper, TrainFreeHead> implements ITrainFreeHeadService {

    @Override
    public TrainFreeHead insertReturnEntity(TrainFreeHead entity) {
        return null;
    }

    @Override
    public TrainFreeHead updateReturnEntity(TrainFreeHead entity) {
        return null;
    }
}
