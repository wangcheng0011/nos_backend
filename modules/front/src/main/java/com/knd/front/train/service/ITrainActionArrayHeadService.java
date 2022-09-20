package com.knd.front.train.service;

import com.knd.common.response.Result;
import com.knd.front.entity.TrainActionArrayHead;
import com.knd.front.train.request.ActionArrayTrainInfoRequest;
import com.knd.mybatis.SuperService;


/**
 * @author will
 */
public interface ITrainActionArrayHeadService extends SuperService<TrainActionArrayHead> {
    Result commitActionArrayTrainInfo(ActionArrayTrainInfoRequest actionArrayTrainInfoRequest);

    Result getActionArrayTrainList(String userId,String currentPage);

    Result getActionArrayTrainDetail(String id);
}
