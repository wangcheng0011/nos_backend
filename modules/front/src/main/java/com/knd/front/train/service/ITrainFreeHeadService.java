package com.knd.front.train.service;

import com.knd.common.response.Result;
import com.knd.front.entity.TrainFreeHead;
import com.knd.front.train.request.FreeTrainInfoRequest;
import com.knd.front.train.request.FreeTrainingInfoRequest;
import com.knd.mybatis.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llx
 * @since 2020-07-03
 */
public interface ITrainFreeHeadService extends SuperService<TrainFreeHead> {
    Result commitFreeTrainInfo(FreeTrainInfoRequest freeTrainInfoRequest);

    Result commitFreeTrainingInfo(FreeTrainingInfoRequest freeTrainingInfoRequest);
}
