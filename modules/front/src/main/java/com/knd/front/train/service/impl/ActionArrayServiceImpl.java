package com.knd.front.train.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.front.entity.ActionArray;
import com.knd.front.entity.ActionArrayDetail;
import com.knd.front.entity.Attach;
import com.knd.front.entity.BaseAction;
import com.knd.front.train.dto.ActionArrayDto;
import com.knd.front.train.dto.FreeTrainDetailDto;
import com.knd.front.train.dto.VideoUrlDto;
import com.knd.front.train.mapper.ActionArrayDetailMapper;
import com.knd.front.train.mapper.ActionArrayMapper;
import com.knd.front.train.mapper.AttachMapper;
import com.knd.front.train.mapper.CourseDetailMapper;
import com.knd.front.train.request.GetActionArrayRequest;
import com.knd.front.train.request.SaveActionArrayRequest;
import com.knd.front.train.request.UpdateActionArrayRequest;
import com.knd.front.train.service.IActionArrayService;
import com.knd.front.user.mapper.BaseActionMapper;
import com.knd.front.user.service.IUserActionPowerTestService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * @author will
 */
@Service
@Log4j2
public class ActionArrayServiceImpl extends ServiceImpl<ActionArrayMapper, ActionArray> implements IActionArrayService {

    @Resource
    private BaseActionMapper baseActionMapper;

    @Resource
    private ActionArrayDetailMapper  actionArrayDetailMapper;


    @Resource
    private CourseDetailMapper courseDetailMapper;

    @Autowired
    private IUserActionPowerTestService iUserActionPowerTestService;

    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    @Value("${upload.FileVideoPath}")
    private String fileVideoPath;


    @Resource
    private AttachMapper attachMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveActionArray(SaveActionArrayRequest saveActionArrayRequest) {
        log.info("saveActionArray saveActionArrayRequest:{{}}",saveActionArrayRequest);
        ActionArray actionArray = new ActionArray();
        actionArray.setId(null);
        actionArray.setUserId(saveActionArrayRequest.getUserId());
        actionArray.setActionArrayName(saveActionArrayRequest.getActionArrayName());
        actionArray.setShareStatus(saveActionArrayRequest.getShareStatus());
        actionArray.setActionQuantity(saveActionArrayRequest.getSaveActionArrayDetailRequests().size()+"");
        actionArray.setCreateBy(saveActionArrayRequest.getUserId());
        actionArray.setLastModifiedBy(saveActionArrayRequest.getUserId());
        actionArray.setCreateDate(LocalDateTime.now());
        BaseAction firstBaseAction = baseActionMapper.selectById(saveActionArrayRequest.getSaveActionArrayDetailRequests().get(0).getActionId());
        actionArray.setCoverAttachId(firstBaseAction.getPicAttachId());
        log.info("saveActionArray actionArray:{{}}",actionArray);
        QueryWrapper<ActionArray> actionArrayQueryWrapper = new QueryWrapper<>();
        actionArrayQueryWrapper.eq("actionArrayName",saveActionArrayRequest.getActionArrayName());
        actionArrayQueryWrapper.eq("userId", saveActionArrayRequest.getUserId());
        actionArrayQueryWrapper.eq("deleted","0");
        ActionArray actionArray1 = baseMapper.selectOne(actionArrayQueryWrapper);
        log.info("saveActionArray actionArray1:{{}}",actionArray1);
        if(StringUtils.isNotEmpty(actionArray1)){
            log.info("saveActionArray 该动作组合名称重复提交");
            return ResultUtil.error("U1998", "该动作组合名称重复提交");
        }
        log.info("saveActionArray actionArray:{{}}",actionArray);
        baseMapper.insert(actionArray);
        AtomicInteger sort = new AtomicInteger(0);
        saveActionArrayRequest.getSaveActionArrayDetailRequests().forEach(e->{
            ActionArrayDetail actionArrayDetail = new ActionArrayDetail();
            actionArrayDetail.setActionId(e.getActionId());
            actionArrayDetail.setActionArrayId(actionArray.getId());
            actionArrayDetail.setAimDuration(e.getAimDuration());
            actionArrayDetail.setAimTimes(e.getAimTimes());
            actionArrayDetail.setSort((sort.incrementAndGet())+"");
            log.info("saveActionArray actionArrayDetail:{{}}",actionArrayDetail);
            actionArrayDetailMapper.insert(actionArrayDetail);
        });
        return ResultUtil.success(actionArray);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateActionArray(UpdateActionArrayRequest updateActionArrayRequest) {
        QueryWrapper<ActionArray> actionArrayQueryWrapper1 = new QueryWrapper<>();
        actionArrayQueryWrapper1.eq("actionArrayName",updateActionArrayRequest.getActionArrayName());
        actionArrayQueryWrapper1.eq("userId", UserUtils.getUserId());
        actionArrayQueryWrapper1.eq("deleted","0");
        actionArrayQueryWrapper1.notExists("select * from action_array where id = '"+updateActionArrayRequest.getId()+"'");
        List<ActionArray> actionArrays = baseMapper.selectList(actionArrayQueryWrapper1);
        log.info("updateActionArray actionArrays:{{}}",actionArrays);
        if(actionArrays.size()>0){
            log.info("saveActionArray 该动作组合名称重复提交");
            return ResultUtil.error("U1998", "该动作组合名称重复提交");
        }
        if(updateActionArrayRequest.getSaveActionArrayDetailRequests() == null ||
                updateActionArrayRequest.getSaveActionArrayDetailRequests().size() == 0 ) {
            ActionArray actionArray = baseMapper.selectById(updateActionArrayRequest.getId());
            actionArray.setActionArrayName(updateActionArrayRequest.getActionArrayName());
            baseMapper.updateById(actionArray);
            return ResultUtil.success();
        }
        ActionArray actionArray = new ActionArray();
        actionArray.setId(updateActionArrayRequest.getId());
        actionArray.setActionArrayName(updateActionArrayRequest.getActionArrayName());
        actionArray.setShareStatus(updateActionArrayRequest.getShareStatus());
        actionArray.setActionQuantity(updateActionArrayRequest.getSaveActionArrayDetailRequests().size()+"");
        BaseAction firstBaseAction = baseActionMapper.selectById(updateActionArrayRequest.getSaveActionArrayDetailRequests().get(0).getActionId());
        actionArray.setCoverAttachId(firstBaseAction.getPicAttachId());
        baseMapper.updateById(actionArray);
        QueryWrapper<ActionArrayDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("actionArrayId",actionArray.getId());
        queryWrapper.select("id");
        List<ActionArrayDetail> actionArrayDetails = actionArrayDetailMapper.selectList(queryWrapper);
        List<String> collect = actionArrayDetails.stream().map(e -> e.getId()).collect(Collectors.toList());
        actionArrayDetailMapper.deleteBatchIds(collect);
        AtomicInteger sort = new AtomicInteger(0);
        updateActionArrayRequest.getSaveActionArrayDetailRequests().forEach(e->{
            ActionArrayDetail actionArrayDetail = new ActionArrayDetail();
            actionArrayDetail.setActionId(e.getActionId());
            actionArrayDetail.setActionArrayId(actionArray.getId());
            actionArrayDetail.setAimDuration(e.getAimDuration());
            actionArrayDetail.setAimTimes(e.getAimTimes());
            actionArrayDetail.setSort((sort.incrementAndGet())+"");
            actionArrayDetailMapper.insert(actionArrayDetail);
        });
        return ResultUtil.success(actionArray);
    }

    @Override
    public Result getUserActionArray(GetActionArrayRequest getActionArrayRequest) {
        Page<ActionArrayDto> tPage = new Page<>(Long.parseLong(getActionArrayRequest.getCurrentPage()), PageInfo.pageSize);
        log.info("getUserActionArray getActionArrayRequest:{{}}",getActionArrayRequest);
//        List<ActionArrayDto> userActionArrayList = baseMapper.getUserActionArrayList(tPage, getActionArrayRequest);
//
//        ArrayListMultimap<String, String> multimap = ArrayListMultimap.create();
//        for(ActionArrayDto actionArrayDto : userActionArrayList) {
//            actionArrayDto.setCoverAttachUrl(fileImagesPath+actionArrayDto.getCoverAttachUrl());
//            multimap.put(actionArrayDto.getId(),fileVideoPath+actionArrayDto.getVideoUrl());
//        }
//        Map<String, Collection<String>> stringCollectionMap = multimap.asMap();
//        List<ActionArrayDto> userActionArrayList1 = new ArrayList<>();
//        for (Map.Entry<String, Collection<String>> p : stringCollectionMap.entrySet()) {
//            for(ActionArrayDto actionArrayDto : userActionArrayList){
//                if(p.getKey().equals(actionArrayDto.getId())) {
//                    ActionArrayDto actionArrayDto1 = new ActionArrayDto();
//                    BeanUtils.copyProperties(actionArrayDto,actionArrayDto1);
//                    actionArrayDto1.setActionVideoUrl((List<String>) p.getValue());
//                    actionArrayDto1.setVideoUrl(null);
//                    userActionArrayList1.add(actionArrayDto1);
//                    break;
//                }
//            }
//        }
        QueryWrapper<ActionArray> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("a.createBy",getActionArrayRequest.getUserId());
        queryWrapper.eq("a.deleted","0");
        queryWrapper.or(e->e.eq("a.shareStatus","1").eq("a.deleted","0"));
        if("ASC".equals(getActionArrayRequest.getSort())) {
            queryWrapper.orderByAsc("length(a."+ getActionArrayRequest.getSortField()+")","a."+getActionArrayRequest.getSortField());
        }else{
            queryWrapper.orderByDesc("length(a."+ getActionArrayRequest.getSortField()+")","a."+getActionArrayRequest.getSortField());
        }
        //List<ActionArray> records = baseMapper.selectPage(tPage, queryWrapper).getRecords();
        List<ActionArrayDto> userActionArrayList = baseMapper.getUserActionArrayList(tPage, queryWrapper);
       // List<ActionArrayDto> userActionArrayList = new ArrayList<>();

        for(ActionArrayDto actionArray : userActionArrayList){
           // ActionArrayDto actionArrayDto = new ActionArrayDto();
           // BeanUtils.copyProperties(actionArray,actionArrayDto);
            actionArray.setCoverAttachUrl(fileImagesPath+actionArray.getCoverAttachUrl());
            QueryWrapper<ActionArrayDetail> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("actionArrayId",actionArray.getId());
            List<ActionArrayDetail> actionArrayDetails = actionArrayDetailMapper.selectList(queryWrapper1);
            ArrayList<VideoUrlDto> videoUrlStrs = new ArrayList<>();
            for(ActionArrayDetail actionArrayDetail : actionArrayDetails) {
                BaseAction baseAction = baseActionMapper.selectById(actionArrayDetail.getActionId());
                if(null!=baseAction){
                    Attach attach1 = attachMapper.selectById(baseAction.getVideoAttachId());
                    videoUrlStrs.add(VideoUrlDto.builder().filePath(fileVideoPath+attach1.getFilePath()).fileSize(attach1.getFileSize()).build());
                }
            }
            actionArray.setActionVideoUrl(videoUrlStrs);
        }
        tPage.setRecords( userActionArrayList);
        return ResultUtil.success(tPage);
    }

    @Override
    public Result getUserActionArrayInfo(String userId,String actionArrayId) {
        QueryWrapper<ActionArrayDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("actionArrayId",actionArrayId);
        queryWrapper.orderByAsc("length(sort)","sort");
        List<ActionArrayDetail> actionArrayDetails = actionArrayDetailMapper.selectList(queryWrapper);
       List<FreeTrainDetailDto> freeTrainDetailDtoList = new ArrayList<>();
        actionArrayDetails.forEach(e->{
           FreeTrainDetailDto freeTrainDetail = courseDetailMapper.getFreeTrainDetail(e.getActionId(), userId);
           if (freeTrainDetail != null) {

               freeTrainDetail.setPicAttachUrl(fileImagesPath + freeTrainDetail.getPicAttachUrl());
               freeTrainDetail.setVideoAttachUrl(fileVideoPath + freeTrainDetail.getVideoAttachUrl());
               freeTrainDetail.setAimDuration(e.getAimDuration());
               freeTrainDetail.setAimTimes(e.getAimTimes());
               if(!"0".equals(freeTrainDetail.getActionType())) {
                   //获取个性化力量值
                   String userTrainPower = iUserActionPowerTestService.getUserTrainPower(userId, freeTrainDetail.getActionType());
                   if(!"-1".equals(userTrainPower)) {
                       freeTrainDetail.setBasePower(userTrainPower);
                   }
               }
               freeTrainDetailDtoList.add(freeTrainDetail);
           }


       });

        return ResultUtil.success(freeTrainDetailDtoList);
    }

    @Override
    public Result deleteUserActionArray(String userId, String actionArrayId) {
        QueryWrapper<ActionArray> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("actionArrayId",actionArrayId);
        ActionArray actionArray = baseMapper.selectById(actionArrayId);
        if(actionArray != null) {
            actionArray.setDeleted("1");
            baseMapper.updateById(actionArray);
        }else{
            return  ResultUtil.error("404","资源不存在");
        }
        return ResultUtil.success();
    }


    @Override
    public ActionArray insertReturnEntity(ActionArray entity) {
        return null;
    }

    @Override
    public ActionArray updateReturnEntity(ActionArray entity) {
        return null;
    }
}
