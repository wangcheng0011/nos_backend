package com.knd.front.train.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.response.CustomResultException;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.front.entity.TrainFreeActHead;
import com.knd.front.entity.TrainFreeHead;
import com.knd.front.entity.TrainFreeItems;
import com.knd.front.entity.TrainFreeTrainHead;
import com.knd.front.train.mapper.TrainFreeActHeadMapper;
import com.knd.front.train.mapper.TrainFreeHeadMapper;
import com.knd.front.train.mapper.TrainFreeTrainHeadMapper;
import com.knd.front.train.request.FreeTrainInfoRequest;
import com.knd.front.train.request.FreeTrainRequest;
import com.knd.front.train.request.FreeTrainingInfoRequest;
import com.knd.front.train.request.TrainFreeItemRequest;
import com.knd.front.train.service.ITrainFreeHeadService;
import com.knd.front.train.service.ITrainFreeItemsService;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
@Service
public class TrainFreeHeadServiceImpl extends ServiceImpl<TrainFreeHeadMapper, TrainFreeHead> implements ITrainFreeHeadService {
    @Autowired
    private TrainFreeHeadMapper trainFreeHeadMapper;
    @Autowired
    private TrainFreeActHeadMapper trainFreeActHeadMapper;
    @Autowired
    private TrainFreeTrainHeadMapper trainFreeTrainHeadMapper;
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
    public Result commitFreeTrainAct(FreeTrainingInfoRequest freeTrainingInfoRequest) {
        try {
            TrainFreeActHead trainFreeActHead = new TrainFreeActHead();
            trainFreeActHead.setUserId(freeTrainingInfoRequest.getUserId());
            trainFreeActHead.setActId(freeTrainingInfoRequest.getActId());
            trainFreeActHead.setAction(freeTrainingInfoRequest.getAction());
            trainFreeActHead.setTotalSeconds(freeTrainingInfoRequest.getTotalSeconds());
            trainFreeActHead.setVedioBeginTime(freeTrainingInfoRequest.getBeginTime());
            trainFreeActHead.setVedioEndTime(freeTrainingInfoRequest.getEndTime());
            trainFreeActHead.setActTrainSeconds(freeTrainingInfoRequest.getActTrainSeconds());
            trainFreeActHead.setFinishSets(freeTrainingInfoRequest.getFinishSets());
            trainFreeActHead.setFinishCounts(freeTrainingInfoRequest.getFinishCounts());
            trainFreeActHead.setFinishTotalPower(freeTrainingInfoRequest.getFinishTotalPower());
            trainFreeActHead.setEquipmentNo(freeTrainingInfoRequest.getEquipmentNo());
            trainFreeActHead.setMaxExplosiveness(freeTrainingInfoRequest.getMaxExplosiveness());
            trainFreeActHead.setAvgExplosiveness(freeTrainingInfoRequest.getAvgExplosiveness());
            trainFreeActHead.setCalorie(freeTrainingInfoRequest.getCalorie());
            trainFreeActHeadMapper.insert(trainFreeActHead);
        } catch (Exception e) {
            throw new CustomResultException("提交失败");
        }
        return ResultUtil.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result commitFreeTrain(FreeTrainRequest freeTrainRequest) {
        //try {
            log.info("commitFreeTrain freeTrainRequest:{{}}",freeTrainRequest);
            TrainFreeTrainHead trainFreeTrainHead = new TrainFreeTrainHead();
            trainFreeTrainHead.setId(UUIDUtil.getShortUUID());
            trainFreeTrainHead.setUserId(freeTrainRequest.getUserId());
            trainFreeTrainHead.setTotalSeconds(freeTrainRequest.getTotalSeconds());
            trainFreeTrainHead.setActTrainSeconds(freeTrainRequest.getActTrainSeconds());
            trainFreeTrainHead.setFinishCounts(freeTrainRequest.getFinishCounts());
            trainFreeTrainHead.setFinishTotalPower(freeTrainRequest.getFinishTotalPower());
            trainFreeTrainHead.setEquipmentNo(freeTrainRequest.getEquipmentNo());
            trainFreeTrainHead.setMaxExplosiveness(freeTrainRequest.getMaxExplosiveness());
            trainFreeTrainHead.setAvgExplosiveness(freeTrainRequest.getAvgExplosiveness());
            trainFreeTrainHead.setCalorie(freeTrainRequest.getCalorie());
            trainFreeTrainHead.setType(freeTrainRequest.getType());
            trainFreeTrainHeadMapper.insert(trainFreeTrainHead);
        //} catch (Exception e) {
        //    throw new CustomResultException("提交失败");
        //}
        return ResultUtil.success();
    }

}
