package com.knd.front.train.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.CustomResultException;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.entity.*;
import com.knd.front.train.dto.ActionArrayTrainListDto;
import com.knd.front.train.dto.TrainActionArrayHeadDto;
import com.knd.front.train.mapper.ProgramHeadDao;
import com.knd.front.train.mapper.ProgramPlanGenerationDao;
import com.knd.front.train.mapper.TrainActionArrayHeadMapper;
import com.knd.front.train.mapper.TrainActionArrayItemsMapper;
import com.knd.front.train.request.ActionArrayTrainInfoRequest;
import com.knd.front.train.request.TrainActionArrayActionRequest;
import com.knd.front.train.request.TrainActionArrayItemRequest;
import com.knd.front.train.service.ITrainActionArrayActionService;
import com.knd.front.train.service.ITrainActionArrayHeadService;
import com.knd.front.train.service.ITrainActionArrayItemsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * @author will
 */
@Service
@Log4j2
public class TrainActionArrayHeadServiceImpl extends ServiceImpl<TrainActionArrayHeadMapper, TrainActionArrayHead> implements ITrainActionArrayHeadService {
    @Autowired
    private TrainActionArrayHeadMapper trainActionArrayHeadMapper;
    @Autowired
    private TrainActionArrayItemsMapper trainActionArrayItemsMapper;
    @Autowired
    private ITrainActionArrayItemsService iTrainActionArrayItemsService;

    @Autowired
    private ITrainActionArrayActionService iTrainActionArrayActionService;

    @Autowired
    private ProgramHeadDao programHeadDao;

    @Autowired
    private ProgramPlanGenerationDao programPlanGenerationDao;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result commitActionArrayTrainInfo(ActionArrayTrainInfoRequest actionArrayTrainInfoRequest) {

        try {
            log.info("commitActionArrayTrainInfo actionArrayTrainInfoRequest:{{}}",actionArrayTrainInfoRequest);
            TrainActionArrayHead trainActionArrayHead = new TrainActionArrayHead();
            trainActionArrayHead.setUserId(actionArrayTrainInfoRequest.getUserId());
            trainActionArrayHead.setActionArrayId(actionArrayTrainInfoRequest.getActionArrayId());
            trainActionArrayHead.setActionArrayName(actionArrayTrainInfoRequest.getActionArrayName());
            trainActionArrayHead.setTotalSeconds(actionArrayTrainInfoRequest.getTotalSeconds());
            trainActionArrayHead.setTrainBeginTime(actionArrayTrainInfoRequest.getBeginTime());
            trainActionArrayHead.setTrainEndTime(actionArrayTrainInfoRequest.getEndTime());
            trainActionArrayHead.setActTrainSeconds(actionArrayTrainInfoRequest.getActTrainSeconds());
            trainActionArrayHead.setFinishSets(actionArrayTrainInfoRequest.getFinishSets());
            trainActionArrayHead.setFinishCounts(actionArrayTrainInfoRequest.getFinishCounts());
            trainActionArrayHead.setFinishTotalPower(actionArrayTrainInfoRequest.getFinishTotalPower());
            trainActionArrayHead.setEquipmentNo(actionArrayTrainInfoRequest.getEquipmentNo());
            trainActionArrayHead.setMaxExplosiveness(actionArrayTrainInfoRequest.getMaxExplosiveness());
            trainActionArrayHead.setAvgExplosiveness(actionArrayTrainInfoRequest.getAvgExplosiveness());
            trainActionArrayHead.setCalorie(actionArrayTrainInfoRequest.getCalorie());
            trainActionArrayHeadMapper.insert(trainActionArrayHead);

            List<TrainActionArrayItemRequest> trainActionArrayItemList = actionArrayTrainInfoRequest.getTrainActionArrayItemList();
            List<TrainActionArrayItems> trainActionArrayItemsList = new LinkedList<>();
            for (int i = 0; i < trainActionArrayItemList.size(); i++) {
                TrainActionArrayItems trainFreeItems = new TrainActionArrayItems();
                trainFreeItems.setActionArraySetNum(trainActionArrayItemList.get(i).getActionArraySetNum());
//                trainFreeItems.setActCountMod(trainActionArrayItemsList.get(i).getActCountMod());
//                trainFreeItems.setActId(trainActionArrayItemsList.get(i).getActId());
//                trainFreeItems.setAction(trainActionArrayItemsList.get(i).getAction());
//                trainFreeItems.setActSetNum(trainActionArrayItemsList.get(i).getActSetNum());
//                trainFreeItems.setActTotalSeconds(trainActionArrayItemsList.get(i).getActTotalSeconds());
//                trainFreeItems.setActLastPowerSetting(trainActionArrayItemsList.get(i).getActLastPowerSetting());
//                trainFreeItems.setActFinishCounts(trainActionArrayItemsList.get(i).getActFinishCounts());
//                trainFreeItems.setActAimCounts(trainActionArrayItemsList.get(i).getActAimCounts());
//                trainFreeItems.setActAimDuration(trainActionArrayItemsList.get(i).getActAimDuration());
//                trainFreeItems.setFinishTotalPower(trainActionArrayItemsList.get(i).getFinishTotalPower());
                trainFreeItems.setTrainActionArrayHeadId(trainActionArrayHead.getId());
                trainActionArrayItemsMapper.insert(trainFreeItems);
                TrainActionArrayItemRequest trainActionArrayItems = trainActionArrayItemList.get(i);
                List<TrainActionArrayAction> trainActionArrayActionList = new ArrayList<>();
               int sort = 0;
                for(TrainActionArrayActionRequest trainActionArrayActionRequest : trainActionArrayItems.getTrainActionArrayActionRequestList()) {
                    TrainActionArrayAction trainActionArrayAction = new TrainActionArrayAction();
                    trainActionArrayAction.setActCountMod(trainActionArrayActionRequest.getActCountMod());
                    trainActionArrayAction.setActId(trainActionArrayActionRequest.getActId());
                    trainActionArrayAction.setAction(trainActionArrayActionRequest.getAction());
                    trainActionArrayAction.setActSetNum(trainActionArrayActionRequest.getActSetNum());
                    trainActionArrayAction.setActTotalSeconds(trainActionArrayActionRequest.getActTotalSeconds());
                    trainActionArrayAction.setActLastPowerSetting(trainActionArrayActionRequest.getActLastPowerSetting());
                    trainActionArrayAction.setActFinishCounts(trainActionArrayActionRequest.getActFinishCounts());
                    trainActionArrayAction.setActAimCounts(trainActionArrayActionRequest.getActAimCounts());
                    trainActionArrayAction.setActAimDuration(trainActionArrayActionRequest.getActAimDuration());
                    trainActionArrayAction.setFinishTotalPower(trainActionArrayActionRequest.getFinishTotalPower());
                    trainActionArrayAction.setTrainActionArrayItemId(trainFreeItems.getId());
                    trainActionArrayAction.setSort((++sort)+"");
                    trainActionArrayActionList.add(trainActionArrayAction);
                }
                iTrainActionArrayActionService.saveBatch(trainActionArrayActionList);
                //trainActionArrayItemsList.add(trainFreeItems);
            }
            //iTrainActionArrayItemsService.saveBatch(trainActionArrayItemsList);
            if(!StringUtils.isEmpty(actionArrayTrainInfoRequest.getTrainProgramPlanGenerationId())) {
                ProgramHeadEntity programHeadEntity = new ProgramHeadEntity();
                programHeadEntity.setTrainHeadInfoId(trainActionArrayHead.getId());
                programHeadEntity.setPlanGenerationId(actionArrayTrainInfoRequest.getTrainProgramPlanGenerationId());
                programHeadEntity.setUserId(actionArrayTrainInfoRequest.getUserId());
                programHeadDao.insert(programHeadEntity);
                ProgramPlanGenerationEntity programPlanGenerationEntity = programPlanGenerationDao.selectById(actionArrayTrainInfoRequest.getTrainProgramPlanGenerationId());
                programPlanGenerationEntity.setTrainFinishFlag("1");
                programPlanGenerationDao.updateById(programPlanGenerationEntity);
            }
        } catch (Exception e) {
            throw new CustomResultException("提交失败");
        }
        return ResultUtil.success();
    }

    @Override
    public Result getActionArrayTrainList(String userId,String currentPage) {
        Page<ActionArrayTrainListDto> tPage = new Page<>(Long.parseLong(currentPage), PageInfo.pageSize);
//        QueryWrapper<TrainActionArrayHead> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("userId",userId);
//        queryWrapper.eq("deleted","0");
//        queryWrapper.select("3 AS trainType,","id AS trainReportId","trainBeginTime AS trainTime"
//                ,"actTrainSeconds AS actTrainSeconds","finishTotalPower AS finishTotalPower");
//        queryWrapper.orderByDesc("trainBeginTime");
        List<ActionArrayTrainListDto> trainActionArrayHeadList = baseMapper.getActionArrayTrainList(tPage,userId);
        tPage.setRecords(trainActionArrayHeadList);
        return ResultUtil.success(tPage);
    }

    @Override
    public Result getActionArrayTrainDetail(String id) {
        TrainActionArrayHeadDto trainActionArrayHeadDetail = baseMapper.getTrainActionArrayHeadDetail(id);
        return ResultUtil.success(trainActionArrayHeadDetail);
    }

    @Override
    public TrainActionArrayHead insertReturnEntity(TrainActionArrayHead entity) {
        return null;
    }

    @Override
    public TrainActionArrayHead updateReturnEntity(TrainActionArrayHead entity) {
        return null;
    }
}
