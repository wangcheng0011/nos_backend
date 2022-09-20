package com.knd.front.train.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.response.CustomResultException;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.entity.TrainFreeHead;
import com.knd.front.entity.TrainFreeItems;
import com.knd.front.entity.TrainFreeTrainingHead;
import com.knd.front.train.mapper.TrainFreeHeadMapper;
import com.knd.front.train.mapper.TrainFreeTrainingHeadMapper;
import com.knd.front.train.request.FreeTrainInfoRequest;
import com.knd.front.train.request.FreeTrainingInfoRequest;
import com.knd.front.train.request.TrainFreeItemRequest;
import com.knd.front.train.service.ITrainFreeHeadService;
import com.knd.front.train.service.ITrainFreeItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-07-03
 */
@Service
public class TrainFreeHeadServiceImpl extends ServiceImpl<TrainFreeHeadMapper, TrainFreeHead> implements ITrainFreeHeadService {
    @Autowired
    private TrainFreeHeadMapper trainFreeHeadMapper;
    @Autowired
    private TrainFreeTrainingHeadMapper trainFreeTrainingHeadMapper;
    @Autowired
    private ITrainFreeItemsService iTrainFreeItemsService;

    @Override
    public TrainFreeHead insertReturnEntity(TrainFreeHead entity) {
        return null;
    }

    @Override
    public TrainFreeHead updateReturnEntity(TrainFreeHead entity) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result commitFreeTrainInfo(FreeTrainInfoRequest freeTrainInfoRequest) {
        try {
            TrainFreeHead trainFreeHead = new TrainFreeHead();
            trainFreeHead.setUserId(freeTrainInfoRequest.getUserId());
            trainFreeHead.setActId(freeTrainInfoRequest.getActId());
            trainFreeHead.setAction(freeTrainInfoRequest.getAction());
            trainFreeHead.setTotalSeconds(freeTrainInfoRequest.getTotalSeconds());
            trainFreeHead.setVedioBeginTime(freeTrainInfoRequest.getBeginTime());
            trainFreeHead.setVedioEndTime(freeTrainInfoRequest.getEndTime());
            trainFreeHead.setActTrainSeconds(freeTrainInfoRequest.getActTrainSeconds());
            trainFreeHead.setFinishSets(freeTrainInfoRequest.getFinishSets());
            trainFreeHead.setFinishCounts(freeTrainInfoRequest.getFinishCounts());
            trainFreeHead.setFinishTotalPower(freeTrainInfoRequest.getFinishTotalPower());
            trainFreeHead.setEquipmentNo(freeTrainInfoRequest.getEquipmentNo());
            trainFreeHead.setMaxExplosiveness(freeTrainInfoRequest.getMaxExplosiveness());
            trainFreeHead.setAvgExplosiveness(freeTrainInfoRequest.getAvgExplosiveness());
            trainFreeHead.setCalorie(freeTrainInfoRequest.getCalorie());
            trainFreeHeadMapper.insert(trainFreeHead);

            List<TrainFreeItemRequest> trainFreeItemList = freeTrainInfoRequest.getTrainFreeItemList();
            List<TrainFreeItems> trainFreeItemsList = new LinkedList<>();
            for (int i = 0; i < trainFreeItemList.size(); i++) {
                TrainFreeItems trainFreeItems = new TrainFreeItems();
                trainFreeItems.setActCountMod(trainFreeItemList.get(i).getActCountMod());
                trainFreeItems.setActId(trainFreeItemList.get(i).getActId());
                trainFreeItems.setAction(trainFreeItemList.get(i).getAction());
                trainFreeItems.setActSetNum(trainFreeItemList.get(i).getActSetNum());
                trainFreeItems.setActTotalSeconds(trainFreeItemList.get(i).getActTotalSeconds());
                trainFreeItems.setActLastPowerSetting(trainFreeItemList.get(i).getActLastPowerSetting());
                trainFreeItems.setActFinishCounts(trainFreeItemList.get(i).getActFinishCounts());
                trainFreeItems.setActAimCounts(trainFreeItemList.get(i).getActAimCounts());
                trainFreeItems.setActAimDuration(trainFreeItemList.get(i).getActAimDuration());
                trainFreeItems.setFinishTotalPower(trainFreeItemList.get(i).getFinishTotalPower());
                trainFreeItems.setTrainFreeHeadId(trainFreeHead.getId());
                trainFreeItemsList.add(trainFreeItems);
            }
            iTrainFreeItemsService.saveBatch(trainFreeItemsList);
        } catch (Exception e) {
            throw new CustomResultException("提交失败");
        }
        return ResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result commitFreeTrainingInfo(FreeTrainingInfoRequest freeTrainingInfoRequest) {
        try {
            TrainFreeTrainingHead trainFreeTrainingHead = new TrainFreeTrainingHead();
            trainFreeTrainingHead.setUserId(freeTrainingInfoRequest.getUserId());
            trainFreeTrainingHead.setActId(freeTrainingInfoRequest.getActId());
            trainFreeTrainingHead.setAction(freeTrainingInfoRequest.getAction());
            trainFreeTrainingHead.setTotalSeconds(freeTrainingInfoRequest.getTotalSeconds());
            trainFreeTrainingHead.setVedioBeginTime(freeTrainingInfoRequest.getBeginTime());
            trainFreeTrainingHead.setVedioEndTime(freeTrainingInfoRequest.getEndTime());
            trainFreeTrainingHead.setActTrainSeconds(freeTrainingInfoRequest.getActTrainSeconds());
            trainFreeTrainingHead.setFinishTotalPower(freeTrainingInfoRequest.getFinishSets());
            trainFreeTrainingHead.setFinishCounts(freeTrainingInfoRequest.getFinishCounts());
            trainFreeTrainingHead.setFinishTotalPower(freeTrainingInfoRequest.getFinishTotalPower());
            trainFreeTrainingHead.setEquipmentNo(freeTrainingInfoRequest.getEquipmentNo());
            trainFreeTrainingHead.setMaxExplosiveness(freeTrainingInfoRequest.getMaxExplosiveness());
            trainFreeTrainingHead.setAvgExplosiveness(freeTrainingInfoRequest.getAvgExplosiveness());
            trainFreeTrainingHead.setCalorie(freeTrainingInfoRequest.getCalorie());
            trainFreeTrainingHeadMapper.insert(trainFreeTrainingHead);
/*
            List<TrainFreeItemRequest> trainFreeItemList = freeTrainInfoRequest.getTrainFreeItemList();
            List<TrainFreeItems> trainFreeItemsList = new LinkedList<>();
            for (int i = 0; i < trainFreeItemList.size(); i++) {
                TrainFreeItems trainFreeItems = new TrainFreeItems();
                trainFreeItems.setActCountMod(trainFreeItemList.get(i).getActCountMod());
                trainFreeItems.setActId(trainFreeItemList.get(i).getActId());
                trainFreeItems.setAction(trainFreeItemList.get(i).getAction());
                trainFreeItems.setActSetNum(trainFreeItemList.get(i).getActSetNum());
                trainFreeItems.setActTotalSeconds(trainFreeItemList.get(i).getActTotalSeconds());
                trainFreeItems.setActLastPowerSetting(trainFreeItemList.get(i).getActLastPowerSetting());
                trainFreeItems.setActFinishCounts(trainFreeItemList.get(i).getActFinishCounts());
                trainFreeItems.setActAimCounts(trainFreeItemList.get(i).getActAimCounts());
                trainFreeItems.setActAimDuration(trainFreeItemList.get(i).getActAimDuration());
                trainFreeItems.setFinishTotalPower(trainFreeItemList.get(i).getFinishTotalPower());
                trainFreeItems.setTrainFreeHeadId(trainFreeHead.getId());
                trainFreeItemsList.add(trainFreeItems);
            }
            iTrainFreeItemsService.saveBatch(trainFreeItemsList);*/
        } catch (Exception e) {
            throw new CustomResultException("提交失败");
        }
        return ResultUtil.success();
    }
}
