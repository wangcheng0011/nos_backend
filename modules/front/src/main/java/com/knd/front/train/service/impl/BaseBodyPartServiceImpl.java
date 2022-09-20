package com.knd.front.train.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.CustomResultException;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.entity.*;
import com.knd.front.home.dto.*;
import com.knd.front.home.mapper.BaseCourseTypeMapper;
import com.knd.front.home.mapper.CourseTypeMapper;
import com.knd.front.login.mapper.UserMapper;
import com.knd.front.train.dto.FilterFreeTrainLabelSettingsDto;
import com.knd.front.train.dto.FilterFreeTrainListDto;
import com.knd.front.train.mapper.*;
import com.knd.front.train.request.*;
import com.knd.front.train.service.IBaseBodyPartService;
import com.knd.front.train.service.ITrainCourseActInfoService;
import com.knd.front.user.mapper.CourseBodyPartMapper;
import com.knd.front.user.mapper.CourseTargetMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-07-02
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class BaseBodyPartServiceImpl extends ServiceImpl<BaseBodyPartMapper, BaseBodyPart> implements IBaseBodyPartService {

    private final CourseTypeMapper courseTypeMapper;
    private final CourseTargetMapper courseTargetMapper;
    private final CourseBodyPartMapper courseBodyPartMapper;
    private final BaseBodyPartMapper baseBodyPartMapper;
    private final BaseCourseTypeMapper baseCourseTypeMapper;
    private final TrainCourseHeadInfoMapper trainCourseHeadInfoMapper;
    private final TrainCourseBlockInfoMapper trainCourseBlockInfoMapper;
    private final UserMapper userMapper;
    private final ITrainCourseActInfoService iTrainCourseActInfoService;
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    @Value("${upload.FileVideoPath}")
    private String fileVideoPath;

    private final ProgramHeadDao programHeadDao;
    private final ProgramPlanGenerationDao programPlanGenerationDao;

    @Override
    public BaseBodyPart insertReturnEntity(BaseBodyPart entity) {
        return null;
    }

    @Override
    public BaseBodyPart updateReturnEntity(BaseBodyPart entity) {
        return null;
    }

    @Override
    public Result getFilterCourseList(FilterCourseListRequest filterCourseListRequest) {
        log.info("getFilterCourseList filterCourseListRequest:{{}}",filterCourseListRequest);
        log.info("getFilterCourseList UserId:{{}}",filterCourseListRequest.getUserId());
        Integer isVip;
        if (StringUtils.isEmpty(filterCourseListRequest.getUserId())) {
            isVip = 0;
        } else {
            isVip = 1;
        }

        List<ListDto> partList = filterCourseListRequest.getPartList();
        List<ListDto> targetList = filterCourseListRequest.getTargetList();
        List<ListDto> typeList = filterCourseListRequest.getTypeList();
        Integer newPageSize = StringUtils.isEmpty(filterCourseListRequest.getPageSize())?PageInfo.pageSize:Integer.parseInt(filterCourseListRequest.getPageSize());
        Page<FilterCourseListDto> tPage = new Page<FilterCourseListDto>(Long.parseLong(filterCourseListRequest.getCurrentPage()),newPageSize);
        //分页数据
        List<FilterCourseListDto> filterCourseList = baseBodyPartMapper.getFilterCourseList(isVip,tPage,typeList, targetList, partList,filterCourseListRequest.getIsPay(),filterCourseListRequest.getUserId());
        log.info("getFilterCourseList filterCourseList:{{}}",filterCourseList);
        filterCourseList.stream().forEach(item ->{
            item.setPicAttachUrl(fileImagesPath + item.getPicAttachUrl());
            item.setVideoAttachUrl(fileVideoPath + item.getVideoAttachUrl());
            List<BaseCourseTypeDto> typeListNew = courseTypeMapper.getCourseType(item.getCourseId());
            List<BaseTargetDto> targetListNew = courseTargetMapper.getCourseTarget(item.getCourseId());
            List<BaseBodyPartDto> partListNew = courseBodyPartMapper.getCoursePart(item.getCourseId());
            item.setTypeList(typeListNew);
            item.setTargetList(targetListNew);
            item.setPartList(partListNew);
        });
        tPage.setRecords(filterCourseList);
        return ResultUtil.success(tPage);
    }

    @Override
    public Result getFilterFreeTrainLabelSettings(String userId) {
        FilterFreeTrainLabelSettingsDto filterFreeTrainLabelSettingsDto = new FilterFreeTrainLabelSettingsDto();
        filterFreeTrainLabelSettingsDto.setPartList(baseCourseTypeMapper.getBaseBodyPartList());
        filterFreeTrainLabelSettingsDto.setTargetList(baseCourseTypeMapper.getBaseTargetList());
        filterFreeTrainLabelSettingsDto.setEquipmentList(baseBodyPartMapper.getBaseEquipment());

        return ResultUtil.success(filterFreeTrainLabelSettingsDto);
    }

    @Override
    public Result getFilterFreeTrainList(FilterFreeTrainListRequest filterFreeTrainListRequest) {
        log.info("getFilterFreeTrainList filterFreeTrainListRequest:{{}}",filterFreeTrainListRequest);
        List<ListDto> equipmentList = filterFreeTrainListRequest.getEquipmentList();
        log.info("getFilterFreeTrainList equipmentList:{{}}",equipmentList);
        List<ListDto> partList = filterFreeTrainListRequest.getPartList();
        log.info("getFilterFreeTrainList partList:{{}}",partList);
        List<ListDto> targetList = filterFreeTrainListRequest.getTargetList();
        log.info("getFilterFreeTrainList targetList:{{}}",targetList);
        Page<FilterFreeTrainListDto> tPage = new Page<FilterFreeTrainListDto>(Long.parseLong(filterFreeTrainListRequest.getCurrentPage()), StringUtils.isEmpty(filterFreeTrainListRequest.getPageSize() )?24:Long.parseLong(filterFreeTrainListRequest.getPageSize()));
//        List<FilterFreeTrainListDto> filterFreeTrainPage = baseBodyPartMapper.getFilterFreeTrainPage(equipmentList, partList, targetList, tPage);
        String userId = filterFreeTrainListRequest.getUserId();
        log.info("getFilterFreeTrainList userId:{{}}",userId);
        List<String> vipStatusList = new ArrayList<>();
        vipStatusList.add("1");
        vipStatusList.add("2");
        vipStatusList.add("3");
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted",0);
        wrapper.in("vipStatus",vipStatusList);
        wrapper.le("vipBeginDate",LocalDate.now());
        wrapper.ge("vipEndDate",LocalDate.now());
        wrapper.eq("id",userId);
        int userNum = userMapper.selectCount(wrapper);
        log.info("getFilterFreeTrainList userNum:{{}}",userNum);
        //不需要查询购买配件
        String isBuy = "0";
        if(userNum == 0){
            //非会员需要查询购买配件所配对得动作信息
            isBuy = "1";
        }

        List<FilterFreeTrainListDto> filterFreeTrainList = baseBodyPartMapper.getFilterFreeTrainList(equipmentList, partList, targetList,tPage,userId,isBuy);
        log.info("getFilterFreeTrainList filterFreeTrainList:{{}}",filterFreeTrainList);
       // List<FilterFreeTrainListDto> data = filterFreeTrainList.stream().skip((Long.parseLong(filterFreeTrainListRequest.getCurrentPage()) - 1) * 24).limit(24).collect(Collectors.toList());
       // log.info("getFilterFreeTrainList data:{{}}",data);
        filterFreeTrainList.stream().forEach(item -> {
                    item.setPicAttachUrl(fileImagesPath + item.getPicAttachUrl());
                    item.setVideoAttachUrl(fileVideoPath + item.getVideoAttachUrl());
                }
        );
        log.info("getFilterFreeTrainList filterFreeTrainList:{{}}",filterFreeTrainList);
        log.info("getFilterFreeTrainList size:{{}}",tPage.getSize());
        log.info("getFilterFreeTrainList total:{{}}",tPage.getTotal());
        tPage.setRecords(filterFreeTrainList);
        return ResultUtil.success(tPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result commitTrainCourseInfo(TrainCourseInfoRequest trainCourseInfoRequest) {
        try {
            log.info("commitTrainCourseInfo trainCourseInfoRequest:{{}}",trainCourseInfoRequest);
            TrainCourseHeadInfo trainCourseHeadInfo = new TrainCourseHeadInfo();

            trainCourseHeadInfo.setUserId(trainCourseInfoRequest.getUserId());
            trainCourseHeadInfo.setCourseHeadId(trainCourseInfoRequest.getCourseHeadId());
            trainCourseHeadInfo.setCourse(trainCourseInfoRequest.getCourse());
            trainCourseHeadInfo.setVedioBeginTime(trainCourseInfoRequest.getVedioBeginTime());
            trainCourseHeadInfo.setVedioEndTime(trainCourseInfoRequest.getVedioEndTime());
            trainCourseHeadInfo.setTotalDurationSeconds(trainCourseInfoRequest.getTotalDurationSeconds());
            trainCourseHeadInfo.setActualTrainSeconds(trainCourseInfoRequest.getActualTrainSeconds());
            trainCourseHeadInfo.setEquipmentNo(trainCourseInfoRequest.getEquipmentNo());
            trainCourseHeadInfo.setMaxExplosiveness(trainCourseInfoRequest.getMaxExplosiveness());
            trainCourseHeadInfo.setAvgExplosiveness(trainCourseInfoRequest.getAvgExplosiveness());
            trainCourseHeadInfo.setCalorie(trainCourseInfoRequest.getCalorie());
            trainCourseHeadInfo.setFinishTotalPower(trainCourseInfoRequest.getFinishTotalPower());
            log.info("commitTrainCourseInfo trainCourseHeadInfo:{{}}",trainCourseHeadInfo);
            trainCourseHeadInfoMapper.insert(trainCourseHeadInfo);

            List<TrainCourseBlockInfoRequest> blockList = trainCourseInfoRequest.getBlockList();
            List<TrainCourseActInfo> trainCourseActInfoList = new LinkedList<>();
            //训练block列表批量新增
            for (int i = 0; i < blockList.size(); i++) {
                TrainCourseBlockInfo trainCourseBlockInfo = new TrainCourseBlockInfo();
                trainCourseBlockInfo.setTrainCourseHeadInfoId(trainCourseHeadInfo.getId());
                trainCourseBlockInfo.setBlockId(blockList.get(i).getBlockId());
                trainCourseBlockInfo.setBlock(blockList.get(i).getBlock());
                trainCourseBlockInfo.setBlockSetNum(blockList.get(i).getBlockSetNum());
                trainCourseBlockInfo.setSets(blockList.get(i).getSets());

                trainCourseBlockInfoMapper.insert(trainCourseBlockInfo);
                //block动作批量新增
                List<TrainCourseActInfoRequest> actionList = blockList.get(i).getActionList();
                //排序号
                int sort = 1;
                for (int j = 0; j < actionList.size(); j++) {
                    TrainCourseActInfo trainCourseActInfo = new TrainCourseActInfo();
                    trainCourseActInfo.setActId(actionList.get(j).getActId());
                    trainCourseActInfo.setActCountMod(actionList.get(j).getActCountMod());
                    trainCourseActInfo.setAction(actionList.get(j).getAction());
                    trainCourseActInfo.setActTotalPower(actionList.get(j).getActTotalPower());
                    trainCourseActInfo.setActTotalSeconds(actionList.get(j).getActTotalSeconds());
                    trainCourseActInfo.setActBasePowerSetting(actionList.get(j).getActBasePowerSetting());
                    trainCourseActInfo.setActFinishSets(actionList.get(j).getActFinishSets());
                    trainCourseActInfo.setActAimSets(actionList.get(j).getActAimSets());
                    trainCourseActInfo.setActAimDuration(actionList.get(j).getActAimDuration());
                    trainCourseActInfo.setTrainCourseBlockInfoId(trainCourseBlockInfo.getId());
                    trainCourseActInfo.setSort(sort + "");
                    trainCourseActInfoList.add(trainCourseActInfo);
                    sort++;
                }
            }

            iTrainCourseActInfoService.saveBatch(trainCourseActInfoList);
            if(!StringUtils.isEmpty(trainCourseInfoRequest.getTrainProgramPlanGenerationId())) {
                ProgramHeadEntity programHeadEntity = new ProgramHeadEntity();
                programHeadEntity.setTrainHeadInfoId(trainCourseHeadInfo.getId());
                programHeadEntity.setPlanGenerationId(trainCourseInfoRequest.getTrainProgramPlanGenerationId());
                programHeadEntity.setUserId(trainCourseInfoRequest.getUserId());
                programHeadDao.insert(programHeadEntity);
                ProgramPlanGenerationEntity programPlanGenerationEntity = programPlanGenerationDao.selectById(trainCourseInfoRequest.getTrainProgramPlanGenerationId());
                programPlanGenerationEntity.setTrainFinishFlag("1");
                programPlanGenerationDao.updateById(programPlanGenerationEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomResultException("提交失败");
        }
        return ResultUtil.success();
    }
}
