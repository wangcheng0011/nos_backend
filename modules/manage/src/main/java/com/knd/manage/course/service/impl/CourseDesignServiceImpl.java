package com.knd.manage.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.uuid.UUIDUtil;
import com.knd.manage.basedata.dto.ImgDto;
import com.knd.manage.common.entity.Attach;
import com.knd.manage.common.mapper.AttachMapper;
import com.knd.manage.common.service.IAttachService;
import com.knd.manage.course.domain.ProgramTypeEnum;
import com.knd.manage.course.dto.ActionListDto;
import com.knd.manage.course.dto.TrainDetailDto;
import com.knd.manage.course.dto.TrainListDto;
import com.knd.manage.course.dto.TrainWeekDetailDto;
import com.knd.manage.course.entity.*;
import com.knd.manage.course.mapper.ActionArrayMapper;
import com.knd.manage.course.mapper.ProgramDayItemMapper;
import com.knd.manage.course.mapper.ProgramWeekDetailMapper;
import com.knd.manage.course.mapper.TrainProgramMapper;
import com.knd.manage.course.request.*;
import com.knd.manage.course.service.CourseDesignService;
import com.knd.manage.course.service.feignInterface.GetTrainProgramFeign;
import com.knd.manage.mall.service.IGoodsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zm
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CourseDesignServiceImpl extends ServiceImpl<TrainProgramMapper,TrainProgramEntity> implements CourseDesignService {

    private final TrainProgramMapper trainProgramMapper;
    private final ActionArrayMapper actionArrayMapper;
    private final GetTrainProgramFeign trainProgramFeign;
    private final AttachMapper attachMapper;
    private final ProgramDayItemMapper programDayItemMapper;
    private final ProgramWeekDetailMapper programWeekDetailMapper;
    private final IAttachService iAttachService;
    private final IGoodsService goodsService;
    //图片路径
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    //图片文件夹路径
    @Value("${OBS.imageFoldername}")
    private String imageFoldername;


    static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public Result getTrainList(String programName,String type, String userId,String current) {
        log.info("--------------我要获取训练计划啦----------------");
        log.info("getTrainList programName:{{}}",programName);
        log.info("getTrainList type:{{}}",type);
        log.info("getTrainList userId:{{}}",userId);
        log.info("getTrainList current:{{}}",current);
        Page<TrainProgramEntity> tPage = new Page<>(Long.parseLong(current), PageInfo.pageSize);
        QueryWrapper<TrainProgramEntity> wrapper = Wrappers.query();
        wrapper.eq("deleted","0");
        wrapper.eq("source","0");
        if(programName!=null){
            wrapper.like("programName",programName);
        }
        if(type!=null){
            wrapper.eq("type",type);
        }
        if(userId!=null){
            wrapper.like("userId",userId);
        }
        List<TrainProgramEntity> trainProgramEntityPage = trainProgramMapper.getList(tPage, wrapper);
        log.info("getTrainList trainProgramEntityPage:{{}}",trainProgramEntityPage);
        for(TrainProgramEntity e : trainProgramEntityPage){
            e.setType(ProgramTypeEnum.values()[Integer.valueOf(e.getType())].getDisplay());
        }
        TrainListDto dto = TrainListDto.builder().total((int)tPage.getTotal()).trainList(trainProgramEntityPage).build();
        log.info("getTrainList trainProgramEntityPage:{{}}",trainProgramEntityPage);
        log.info("--------------我要结束获取训练计划啦----------------");
        return ResultUtil.success(dto);
    }

    @Override
    public Result deleteTrain(String id) {
        TrainProgramEntity trainProgramEntity = trainProgramMapper.selectById(id);
        trainProgramEntity.setDeleted("1");
        trainProgramMapper.updateById(trainProgramEntity);
        return ResultUtil.success();
    }

    @Override
    public Result addTrain(AddTrainRequest request) {
        log.info("--------------我要新增训练计划啦----------------");
        log.info("addTrain request:{{}}",request);
        QueryWrapper<TrainProgramEntity> qw = new QueryWrapper<>();
        qw.eq("deleted","0");
        qw.eq("programName",request.getProgramName());
        int count = baseMapper.selectCount(qw);
        log.info("addTrain count:{{}}",count);
        if(count > 0){
            //业务主键重复
            return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
        }
        List<TrainProgramWeekDetailRequest> trainProgramWeekDetailRequests = new ArrayList<>();

        List<TrainDetailRequest> trainDetailRequest = request.getDetailList();
        log.info("addTrain trainDetailRequest:{{}}",trainDetailRequest);
        getDetailRequests(trainProgramWeekDetailRequests,trainDetailRequest,"周一",1);
        getDetailRequests(trainProgramWeekDetailRequests,trainDetailRequest,"周二",2);
        getDetailRequests(trainProgramWeekDetailRequests,trainDetailRequest,"周三",3);
        getDetailRequests(trainProgramWeekDetailRequests,trainDetailRequest,"周四",4);
        getDetailRequests(trainProgramWeekDetailRequests,trainDetailRequest,"周五",5);
        getDetailRequests(trainProgramWeekDetailRequests,trainDetailRequest,"周六",6);
        getDetailRequests(trainProgramWeekDetailRequests,trainDetailRequest,"周日",7);

        SaveTrainProgramRequest saveTrainProgramRequest = SaveTrainProgramRequest.builder()
                .userId(request.getUserId())
                .programName(request.getProgramName())
                .trainWeekNum(Integer.valueOf(request.getTrainWeekNum()))
                .picAttachUrl(request.getPicAttachUrl())
                .source("0")
                .type(request.getType())
                .trainProgramWeekDetailRequests(trainProgramWeekDetailRequests).build();


        log.info("addTrain saveTrainProgramRequest:{{}}",saveTrainProgramRequest);
        return trainProgramFeign.saveTrainProgram(saveTrainProgramRequest);
    }

    @Override
    public Result editTrain(AddTrainRequest request) {
        log.info("editTrain userId:{{}}",request.getUserId());
        log.info("editTrain request:{{}}",request);
        QueryWrapper<TrainProgramEntity> qw = new QueryWrapper<>();
        qw.eq("deleted","0");
        qw.eq("id",request.getProgramId());
        TrainProgramEntity trainProgramEntity = baseMapper.selectOne(qw);
        log.info("editTrain trainProgramEntity:{{}}",trainProgramEntity);
        if (trainProgramEntity == null) {
            //没有该id的内容,参数异常
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if(!trainProgramEntity.getProgramName().equals(request.getProgramName())){
            //查重
            qw.clear();
            qw.eq("deleted","0");
            qw.eq("programName",request.getProgramName());
            int count = baseMapper.selectCount(qw);
            if(count > 0){
                //业务主键重复
                return ResultUtil.error(ResultEnum.SERVICE_KEY_ERROR);
            }
        }
        attachMapper.deleteById(trainProgramEntity.getPicAttachId());

        List<String> dayIds = new ArrayList<>();

        QueryWrapper<ProgramWeekDetailEntity> weekQw = new QueryWrapper<ProgramWeekDetailEntity>()
                .eq("programId",request.getProgramId())
                .eq("deleted","0");
        log.info("editTrain weekQw:{{}}",weekQw);
        List<ProgramWeekDetailEntity> programWeekDetailEntities = programWeekDetailMapper.selectList(weekQw);
        for(ProgramWeekDetailEntity week : programWeekDetailEntities){
            QueryWrapper<ProgramDayItemEntity> dayQw = new QueryWrapper<ProgramDayItemEntity>()
                    .eq("trainProgramWeekDetailId",week.getId())
                    .eq("deleted","0");
            List<ProgramDayItemEntity> programDayItemEntities = programDayItemMapper.selectList(dayQw);
            for(ProgramDayItemEntity day : programDayItemEntities){
                dayIds.add(day.getId());
            }
        }
        if(dayIds.size()>0){
            programDayItemMapper.deleteBatchIds(dayIds);
        }
        programWeekDetailMapper.delete(weekQw);

        //保存选中图片
        Attach picAttachUrl = goodsService.saveAttach(request.getUserId(), request.getPicAttachUrl().getPicAttachName()
                , request.getPicAttachUrl().getPicAttachNewName(), request.getPicAttachUrl().getPicAttachSize());

        log.info("editTrain picAttachUrl:{{}}",picAttachUrl);
        List<TrainProgramWeekDetailRequest> trainProgramWeekDetailRequests = new ArrayList<>();
        List<TrainDetailRequest> trainDetailRequest = request.getDetailList();
        getDetailRequests(trainProgramWeekDetailRequests,trainDetailRequest,"周一",1);
        getDetailRequests(trainProgramWeekDetailRequests,trainDetailRequest,"周二",2);
        getDetailRequests(trainProgramWeekDetailRequests,trainDetailRequest,"周三",3);
        getDetailRequests(trainProgramWeekDetailRequests,trainDetailRequest,"周四",4);
        getDetailRequests(trainProgramWeekDetailRequests,trainDetailRequest,"周五",5);
        getDetailRequests(trainProgramWeekDetailRequests,trainDetailRequest,"周六",6);
        getDetailRequests(trainProgramWeekDetailRequests,trainDetailRequest,"周日",7);

        SaveTrainProgramRequest saveTrainProgramRequest = SaveTrainProgramRequest.builder()
                .programName(request.getProgramName())
                .trainWeekNum(Integer.valueOf(request.getTrainWeekNum()))
                .picAttachUrl(request.getPicAttachUrl())
                //.source("1")
                .source("0")
                .type(request.getType())
                .trainProgramWeekDetailRequests(trainProgramWeekDetailRequests).build();

        log.info("editTrain saveTrainProgramRequest:{{}}",saveTrainProgramRequest);
        TrainProgramEntity programEntity = new TrainProgramEntity();
        BeanUtils.copyProperties(saveTrainProgramRequest,programEntity);
        programEntity.setId(request.getProgramId());
        programEntity.setTrainWeekNum(saveTrainProgramRequest.getTrainWeekNum()+"");
        programEntity.setTrainWeekDayNum(saveTrainProgramRequest.getTrainProgramWeekDetailRequests().size()+"");
        programEntity.setType(saveTrainProgramRequest.getType());
        programEntity.setPicAttachId(picAttachUrl.getId());
        programEntity.setLastModifiedBy(request.getUserId());
        programEntity.setLastModifiedDate(LocalDateTime.now());
        programEntity.setDeleted("0");
        log.info("editTrain programEntity:{{}}",programEntity);
        baseMapper.updateById(programEntity);





        //保存周明细
        saveTrainProgramRequest.getTrainProgramWeekDetailRequests().forEach(e->{
            ProgramWeekDetailEntity programWeekDetailEntity = new ProgramWeekDetailEntity();
            programWeekDetailEntity.setId(UUIDUtil.getShortUUID());
            programWeekDetailEntity.setProgramId(request.getProgramId());
            programWeekDetailEntity.setWeekDayName(e.getWeekDayName()+"");
            programWeekDetailEntity.setCreateBy(request.getUserId());
            programWeekDetailEntity.setCreateDate(LocalDateTime.now());
            programWeekDetailEntity.setLastModifiedDate(LocalDateTime.now());
            programWeekDetailEntity.setDeleted("0");
            log.info("editTrain programWeekDetailEntity:{{}}",programWeekDetailEntity);
            programWeekDetailMapper.insert(programWeekDetailEntity);
            //保存训练日训练项目
            e.getTrainProgramDayItemRequests().forEach(i->{
                ProgramDayItemEntity programDayItemEntity = new ProgramDayItemEntity();
                programDayItemEntity.setId(UUIDUtil.getShortUUID());
                programDayItemEntity.setTrainProgramWeekDetailId(programWeekDetailEntity.getId());
                programDayItemEntity.setItemType(i.getItemType());
                programDayItemEntity.setItemId(i.getItemId());
                programDayItemEntity.setDeleted("0");
                programDayItemEntity.setCreateDate(LocalDateTime.now());
                programDayItemEntity.setLastModifiedDate(LocalDateTime.now());
                programDayItemEntity.setDeleted("0");
                log.info("editTrain programDayItemEntity:{{}}",programDayItemEntity);
                programDayItemMapper.insert(programDayItemEntity);
            });
        });
       return ResultUtil.success();
    }

    @Override
    public Result getDetail(String id) {
        TrainDetailDto dto = new TrainDetailDto();
        QueryWrapper<TrainProgramEntity> qw = new QueryWrapper<>();
        qw.eq("deleted","0");
        qw.eq("id",id);
        TrainProgramEntity trainProgramEntity = baseMapper.selectOne(qw);
        List<TrainWeekDetailDto> detail = trainProgramMapper.getDetail(id);
        BeanUtils.copyProperties(trainProgramEntity,dto);
        dto.setPicAttach(getImgDto(trainProgramEntity.getPicAttachId()));
        dto.setTrainDetailList(detail);
        return ResultUtil.success(dto);
    }

    private void getDetailRequests(List<TrainProgramWeekDetailRequest> trainProgramWeekDetailRequests,
                                                   List<TrainDetailRequest> trainDetailRequest,
                                                   String weekDayName,Integer weekDay){
        List<TrainProgramDayItemRequest> trainProgramDayItemRequests = new ArrayList<>();
        trainDetailRequest.stream().filter(d -> weekDayName.equals(d.getWeekDayName())).forEach(de ->{
            TrainProgramDayItemRequest build = TrainProgramDayItemRequest.builder().itemId(de.getItemId()).itemType(de.getItemType()).build();
            trainProgramDayItemRequests.add(build);
        });
        if(trainProgramDayItemRequests.size()>0){
            trainProgramWeekDetailRequests.add(TrainProgramWeekDetailRequest.builder()
                    .weekDayName(weekDay).trainProgramDayItemRequests(trainProgramDayItemRequests).build());
        }
    }

    @Override
    public Result getActionList(String actionArrayName,String current) {
        Page<ActionArrayEntity> tPage = new Page<>(Long.parseLong(current), PageInfo.pageSize);
        QueryWrapper<ActionArrayEntity> wrapper = Wrappers.query();
        wrapper.eq("deleted","0");
        if(actionArrayName!=null){
            wrapper.like("actionArrayName",actionArrayName);
        }
        List<ActionArrayEntity> actionArrayPage = actionArrayMapper.getList(tPage, wrapper);
        ActionListDto dto = ActionListDto.builder().total((int) tPage.getTotal()).actionArrayList(actionArrayPage).build();
        return ResultUtil.success(dto);
    }

    @Override
    public TrainProgramEntity insertReturnEntity(TrainProgramEntity entity) {
        return null;
    }

    @Override
    public TrainProgramEntity updateReturnEntity(TrainProgramEntity entity) {
        return null;
    }

    public ImgDto getImgDto(String urlId){
        //根据id获取图片信息
        Attach aPi = iAttachService.getInfoById(urlId);
        ImgDto imgDto = new ImgDto();
        if (aPi != null) {
            imgDto.setPicAttachUrl(fileImagesPath + aPi.getFilePath());
            imgDto.setPicAttachSize(aPi.getFileSize());
            String[] strs = (aPi.getFilePath()).split("\\?");
            imgDto.setPicAttachNewName(imageFoldername + strs[0]);
            imgDto.setPicAttachName(aPi.getFileName());
        }
        return imgDto;
    }
}
