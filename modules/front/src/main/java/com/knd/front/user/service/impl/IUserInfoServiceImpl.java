package com.knd.front.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.basic.StringUtils;
import com.knd.common.em.LabelTypeEnum;
import com.knd.common.em.VipEnum;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.domain.UserPayCheckEnum;
import com.knd.front.entity.*;
import com.knd.front.home.dto.BaseBodyPartDto;
import com.knd.front.home.dto.BaseTargetDto;
import com.knd.front.home.mapper.CourseHeadMapper;
import com.knd.front.live.entity.BaseDifficulty;
import com.knd.front.live.mapper.BaseDifficultyMapper;
import com.knd.front.login.mapper.UserDetailMapper;
import com.knd.front.login.mapper.UserMapper;
import com.knd.front.social.dto.LabelDto;
import com.knd.front.social.mapper.UserLabelMapper;
import com.knd.front.train.mapper.*;
import com.knd.front.user.dto.*;
import com.knd.front.user.entity.UserTrainCourseEntity;
import com.knd.front.user.mapper.*;
import com.knd.front.user.request.UserTrainDataQueryRequest;
import com.knd.front.user.request.UserTrainInfoQueryRequest;
import com.knd.front.user.service.IUserInfoService;
import com.knd.front.user.service.IUserPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/3
 * @Version 1.0
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class IUserInfoServiceImpl extends ServiceImpl<UserInfoMapper, User> implements IUserInfoService {
    private final UserInfoMapper userInfoMapper;
    private final UserDetailMapper userDetailMapper;
    private final TrainCourseHeadInfoMapper trainCourseHeadInfoMapper;
    private final TrainFreeHeadMapper trainFreeHeadMapper;
    private final TrainActionArrayHeadMapper trainActionArrayHeadMapper;
    private final TrainFreeTrainingHeadMapper trainFreeTrainingHeadMapper;
    private final UserActionPowerTestMapper userActionPowerTestMapper;
    private final IUserPayService userPayService;
    private final UserCourseMapper userCourseMapper;
    private final AttachMapper attachMapper;
    private final BaseTargetMapper baseTargetMapper;
    private final CourseTargetMapper courseTargetMapper;
    private final BaseBodyPartMapper baseBodyPartMapper;
    private final CourseBodyPartMapper courseBodyPartMapper;
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    private final CourseHeadMapper courseHeadMapper;
    private final BaseDifficultyMapper baseDifficultyMapper;
    private final UserMapper userMapper;
    private final UserLabelMapper userLabelMapper;


    @Override
    public Result getUserCenterInfo(String userId) {
        log.info("getUserCenterInfo userId:{{}}",userId);
        User user1 = userMapper.selectById(userId);
        if(StringUtils.isNotEmpty(user1)){
            LocalDate vipEndDate = user1.getVipEndDate();
            if(null!=vipEndDate&&vipEndDate.isBefore(LocalDate.now())) {
                user1.setVipStatus(VipEnum.ORDINARY_VIP.getCode());
                userMapper.updateById(user1);
            }
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", userId);
        userQueryWrapper.eq("deleted", 0);
     //   userQueryWrapper.select("nickName", "trainLevel", "trainPeriodBeginTime",
      //          "vipStatus","masterId","vipBeginDate","vipEndDate");
        User user = userInfoMapper.selectOne(userQueryWrapper);
        if (user==null){
            user = new User();
        }
        QueryWrapper<UserDetail> userDetailQueryWrapper = new QueryWrapper<>();
        userDetailQueryWrapper.eq("userId", userId);
        userDetailQueryWrapper.eq("deleted", 0);
        UserDetail userDetail = userDetailMapper.selectOne(userDetailQueryWrapper);
        log.info("getUserCenterInfo userDetail:{{}}",userDetail);
        if (userDetail==null){
            userDetail = new UserDetail();
        }
        String headPicUrl = "";
        Attach attach = attachMapper.selectById(userDetail.getHeadPicUrlId());
        if(attach!=null){
            headPicUrl = fileImagesPath+attach.getFilePath();
        }
        QueryWrapper<TrainCourseHeadInfo> trainCourseHeadInfoQueryWrapper = new QueryWrapper<>();
        trainCourseHeadInfoQueryWrapper.eq("userId", userId);
        trainCourseHeadInfoQueryWrapper.eq("deleted", 0);
        trainCourseHeadInfoQueryWrapper.last("limit 1");
        trainCourseHeadInfoQueryWrapper.select("ifnull(sum(calorie),0) as calorie,ifnull(sum(finishTotalPower),0) as finishTotalPower,ifnull(sum(totalDurationSeconds),0) as totalDurationSeconds", "ifnull(sum(actualTrainSeconds),0)as actualTrainSeconds");
        TrainCourseHeadInfo trainCourseHeadInfo = trainCourseHeadInfoMapper.selectOne(trainCourseHeadInfoQueryWrapper);
        if (trainCourseHeadInfo==null){
            trainCourseHeadInfo = new TrainCourseHeadInfo();
        }
        QueryWrapper<TrainFreeHead> trainFreeHeadQueryWrapper = new QueryWrapper<>();
        trainFreeHeadQueryWrapper.eq("userId", userId);
        trainFreeHeadQueryWrapper.eq("deleted", 0);
        trainFreeHeadQueryWrapper.last("limit 1");
        trainFreeHeadQueryWrapper.select("ifnull(sum(calorie),0) as calorie,ifnull(sum(finishTotalPower),0) as finishTotalPower,ifnull(sum(totalSeconds),0) as totalSeconds", "ifnull(sum(actTrainSeconds),0) as actTrainSeconds");
        TrainFreeHead trainFreeHead = trainFreeHeadMapper.selectOne(trainFreeHeadQueryWrapper);
        if (trainFreeHead==null){
            trainFreeHead = new TrainFreeHead();
        }
        //TODO 唯一索引怎么定
        QueryWrapper<TrainActionArrayHead> trainActionArrayHeadQueryWrapper = new QueryWrapper<>();
        trainActionArrayHeadQueryWrapper.eq("userId", userId);
        trainActionArrayHeadQueryWrapper.eq("deleted", 0);
        trainActionArrayHeadQueryWrapper.last("limit 1");
        trainActionArrayHeadQueryWrapper.select("ifnull(sum(calorie),0) as calorie,ifnull(sum(finishTotalPower),0) as finishTotalPower,ifnull(sum(totalSeconds),0) as totalSeconds", "ifnull(sum(actTrainSeconds),0) as actTrainSeconds");
        TrainActionArrayHead trainActionArrayHead = trainActionArrayHeadMapper.selectOne(trainActionArrayHeadQueryWrapper);
        if (trainActionArrayHead==null){
            trainActionArrayHead = new TrainActionArrayHead();
        }
        QueryWrapper<TrainFreeTrainingHead> trainFreeTrainingHeadInfoQueryWrapper = new QueryWrapper<>();
        trainFreeTrainingHeadInfoQueryWrapper.eq("userId", userId);
        trainFreeTrainingHeadInfoQueryWrapper.eq("deleted", 0);
        trainFreeTrainingHeadInfoQueryWrapper.last("limit 1");
        trainFreeTrainingHeadInfoQueryWrapper.select("ifnull(sum(calorie),0) as calorie,ifnull(sum(finishTotalPower),0) as finishTotalPower,ifnull(sum(totalSeconds),0) as totalSeconds", "ifnull(sum(actTrainSeconds),0)as actTrainSeconds");
        TrainFreeTrainingHead trainFreeTrainingHead= trainFreeTrainingHeadMapper.selectOne(trainFreeTrainingHeadInfoQueryWrapper);
        if (trainFreeTrainingHead==null){
            trainFreeTrainingHead = new TrainFreeTrainingHead();
        }
        //查询用户课程训练总力
        //String courseHeadTotalTrainKg = trainCourseHeadInfoMapper.getCourseHeadTotalTrainKg(userId);
//        String courseHeadTotalTrainKg = "";
//        Integer courseHeadTotalTrainKgInt = 0;
//        Integer totalCalorie = 0;
//        QueryWrapper<TrainCourseHeadInfo> trainCourseHeadInfoQueryWrapper1 = new QueryWrapper<>();
//        trainCourseHeadInfoQueryWrapper1.eq("userId", userId);
//        trainCourseHeadInfoQueryWrapper1.eq("deleted", 0);
//        List<TrainCourseHeadInfo> trainCourseHeadInfos = trainCourseHeadInfoMapper.selectList(trainCourseHeadInfoQueryWrapper1);
//        for(int i = 0;i<trainCourseHeadInfos.size();i++) {
//            if("".equals(e.getFinishTotalPower())) {
//                courseHeadTotalTrainKgInt+=0;
//
//            }else{
//                courseHeadTotalTrainKgInt+=Integer.parseInt(trainCourseHeadInfos.get(i).getFinishTotalPower());
//            }
//        }
//        courseHeadTotalTrainKg = courseHeadTotalTrainKgInt+"";
//        if (StringUtils.isEmpty(courseHeadTotalTrainKg)){
//            courseHeadTotalTrainKg = "0";
//        }
        //查询用户动作总力
//        String freeHeadTotalTrainKg = trainCourseHeadInfoMapper.getFreeHeadTotalTrainKg(userId);
//        if (StringUtils.isEmpty(freeHeadTotalTrainKg)) {
//            freeHeadTotalTrainKg = "0";
//        }
        //查询用户上一个等级
        String latestTrainLevel = trainCourseHeadInfoMapper.getLatestTrainLevel(userId);
        if (StringUtils.isEmpty(latestTrainLevel)){
            latestTrainLevel = "";
        }
        UserCenterInfoDto userCenterInfoDto = new UserCenterInfoDto();
        //完成总力
        userCenterInfoDto.setTotalTrainKg((Integer.parseInt(trainCourseHeadInfo.getFinishTotalPower())+Integer.parseInt(trainFreeHead.getFinishTotalPower())+Integer.parseInt(trainActionArrayHead.getFinishTotalPower()))+(Integer.parseInt(trainFreeTrainingHead.getFinishTotalPower())+""));
        //上一周期
        userCenterInfoDto.setLatestTrainLevel(latestTrainLevel);

        userCenterInfoDto.setNickName(user.getNickName());
        userCenterInfoDto.setTrainLevel(user.getTrainLevel()+"");
        userCenterInfoDto.setTrainPeriodBeginTime(user.getTrainPeriodBeginTime()+"");
        userCenterInfoDto.setVipStatus(user.getVipStatus());
        userCenterInfoDto.setVipBeginDate(user.getVipBeginDate());
        userCenterInfoDto.setVipEndDate(user.getVipEndDate());
        if(StringUtils.isEmpty(userDetail.getBmi())){
            userCenterInfoDto.setBmi("0");
        }else {
            userCenterInfoDto.setBmi(new BigDecimal(userDetail.getBmi()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
        userCenterInfoDto.setPerSign(userDetail.getPerSign());
        String totalSeconds = String.valueOf(StringUtils.StringToDouble(trainFreeHead.getTotalSeconds()) +StringUtils.StringToDouble(trainCourseHeadInfo.getTotalDurationSeconds())+StringUtils.StringToDouble(trainActionArrayHead.getTotalSeconds())+StringUtils.StringToDouble(trainFreeTrainingHead.getTotalSeconds()));
        userCenterInfoDto.setTotalSeconds(totalSeconds);
        String actTrainSeconds = String.valueOf(StringUtils.StringToDouble(trainFreeHead.getActTrainSeconds()) + StringUtils.StringToDouble(trainCourseHeadInfo.getActualTrainSeconds())+StringUtils.StringToDouble(trainActionArrayHead.getActTrainSeconds())+StringUtils.StringToDouble(trainFreeTrainingHead.getActTrainSeconds()));
        userCenterInfoDto.setTotalCalorie(String.valueOf(StringUtils.StringToDouble(trainCourseHeadInfo.getCalorie()) + StringUtils.StringToDouble(trainFreeHead.getCalorie())+StringUtils.StringToDouble(trainActionArrayHead.getCalorie())+StringUtils.StringToDouble(trainFreeTrainingHead.getCalorie())));
        QueryWrapper<UserActionPowerTest> userActionPowerTestQueryWrapper = new QueryWrapper<>();
        userActionPowerTestQueryWrapper.eq("userId",userId);
        userActionPowerTestQueryWrapper.eq("deleted","0");
        Integer userActionPowerTestCount = userActionPowerTestMapper.selectCount(userActionPowerTestQueryWrapper);
        userCenterInfoDto.setTestTrain(userActionPowerTestCount+"");
        userCenterInfoDto.setHeadPicUrl(headPicUrl);

        List<String> labelTypeList = new ArrayList<>();
        labelTypeList.add(LabelTypeEnum.DANCE.ordinal()+"");
        labelTypeList.add(LabelTypeEnum.BODYBUILDING.ordinal()+"");
        labelTypeList.add(LabelTypeEnum.POWER.ordinal()+"");
        labelTypeList.add(LabelTypeEnum.PILATES.ordinal()+"");
        labelTypeList.add(LabelTypeEnum.YOGA.ordinal()+"");
        labelTypeList.add(LabelTypeEnum.STRENGTH.ordinal()+"");
        //默认不是教练
        userCenterInfoDto.setIsCoach("0");
        List<String> labelList = new ArrayList<>();
        //标签信息
        List<LabelDto> labelDtos = userLabelMapper.getLabelList(userId);
        for (LabelDto label : labelDtos){
            labelList.add(label.getLabel());
            //只有有一个教练类型得标签就改成教练
            if(labelTypeList.contains(label.getType())){
                userCenterInfoDto.setIsCoach("1");
            }
        }
        return ResultUtil.success(userCenterInfoDto);
    }




    @Override
    public Result getUserTrainCourseDetail(String userId, String trainReportId) {

        TrainCourseHeadInfoDto getUserCourseHeadInfo = userInfoMapper.getUserCourseHeadInfo(userId, trainReportId);
        if (null ==getUserCourseHeadInfo){
            return ResultUtil.success(new TrainCourseHeadInfoDto());
        }
        TrainCourseHeadInfoDto trainCourseHeadInfoDto = new TrainCourseHeadInfoDto();
        trainCourseHeadInfoDto.setCourse(getUserCourseHeadInfo.getCourse());
        trainCourseHeadInfoDto.setActualTrainSeconds(getUserCourseHeadInfo.getActualTrainSeconds());
        trainCourseHeadInfoDto.setCommitTrainTime(getUserCourseHeadInfo.getCommitTrainTime());
        trainCourseHeadInfoDto.setTotalDurationSeconds(getUserCourseHeadInfo.getTotalDurationSeconds());
        trainCourseHeadInfoDto.setFinishTotalPower(getUserCourseHeadInfo.getFinishTotalPower());
        trainCourseHeadInfoDto.setMaxExplosiveness(getUserCourseHeadInfo.getMaxExplosiveness());
        trainCourseHeadInfoDto.setAvgExplosiveness(getUserCourseHeadInfo.getAvgExplosiveness());
        trainCourseHeadInfoDto.setCalorie(getUserCourseHeadInfo.getCalorie());
        List<TrainCourseBlockInfoDto> getUserCourseBlockInfoList  = userInfoMapper.getUserCourseBlockInfoList(getUserCourseHeadInfo.getId());
        trainCourseHeadInfoDto.setBlockList(getUserCourseBlockInfoList);
        for (TrainCourseBlockInfoDto t :getUserCourseBlockInfoList){
           List<TrainCourseActInfoDto> getUserCourseActInfoList =  userInfoMapper.getUserCourseActInfoList(t.getId());

           t.setActionList(getUserCourseActInfoList);

        }
//        userInfoMapper.getUserTrainCourseDetail(userId, trainReportId)
        return ResultUtil.success(trainCourseHeadInfoDto);
    }

    @Override
    public Result getUserTrainFreeDetail(String userId, String trainReportId) {
        TrainFreeHeadDto userTrainFreeDetail = userInfoMapper.getUserTrainFreeDetail(userId, trainReportId);
        if (null ==userTrainFreeDetail){
            return ResultUtil.success(new TrainFreeHeadDto());
        }
        TrainFreeHeadDto trainFreeHeadDto = new TrainFreeHeadDto();
        trainFreeHeadDto.setCommitTrainTime(userTrainFreeDetail.getCommitTrainTime());
        trainFreeHeadDto.setAction(userTrainFreeDetail.getAction());
        trainFreeHeadDto.setTotalSeconds(userTrainFreeDetail.getTotalSeconds());
        trainFreeHeadDto.setActTrainSeconds(userTrainFreeDetail.getActTrainSeconds());
        trainFreeHeadDto.setFinishSets(userTrainFreeDetail.getFinishSets());
        trainFreeHeadDto.setFinishCounts(userTrainFreeDetail.getFinishCounts());
        trainFreeHeadDto.setFinishTotalPower(userTrainFreeDetail.getFinishTotalPower());
        trainFreeHeadDto.setMaxExplosiveness(userTrainFreeDetail.getMaxExplosiveness());
        trainFreeHeadDto.setAvgExplosiveness(userTrainFreeDetail.getAvgExplosiveness());
        trainFreeHeadDto.setCalorie(userTrainFreeDetail.getCalorie());
        trainFreeHeadDto.setTrainFreeItemList(userTrainFreeDetail.getTrainFreeItemList());
        return ResultUtil.success(trainFreeHeadDto);
    }

    @Override
    public Result getUserCourseInfo(String userId, String currentPage) {

        Page<UserCourseInfoDto> tPage = new Page<UserCourseInfoDto>(Long.parseLong(currentPage), PageInfo.pageSize);
        List<UserCourseInfoDto> userCourseInfoList = userInfoMapper.getUserCourseInfo(tPage, userId);
        if (StringUtils.isEmpty(userCourseInfoList)){
            userCourseInfoList = new ArrayList<>();
        }
//        IPage<UserCourseInfoDto> userCourseInfoList = userInfoMapper.getUserCourseInfo(tPage,userId);

        userCourseInfoList.stream().forEach(item -> {
            if (StringUtils.isNotEmpty(item.getVedioEndTime())) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime beginTime = LocalDateTime.parse(item.getVedioBeginTime(), df);
                LocalDateTime endTime = LocalDateTime.parse(item.getVedioEndTime(), df);
                long time = DateUtils.durationMillis(beginTime, endTime);
                Integer a;
                if ((int)time%60000==0){
                    a= (int)time/60000;
                }else {
                    a=((int)time/60000)+1;
                }
                item.setVideoDuration(a+"");
//                if (time < 60000) {
//                    item.setVideoDuration("小一分钟");
//                } else {
//                    item.setVideoDuration(DateUtils.getGapTime(time)+"");
//                }
            }else {
                item.setVideoDuration(null);
            }
            item.setPicAttachUrl(fileImagesPath+item.getPicAttachUrl());

            List<BaseTargetDto> targetList = new ArrayList<>();
            List<CourseTarget> courseTargets = courseTargetMapper.selectList(new QueryWrapper<CourseTarget>().eq("courseId", item.getCourseId()).eq("deleted", "0"));
            for(CourseTarget target : courseTargets){
                BaseTarget baseTarget = baseTargetMapper.selectById(target.getTargetId());
                BaseTargetDto targetDto = new BaseTargetDto();
                targetDto.setId(target.getTargetId());
                targetDto.setName(baseTarget!=null ? baseTarget.getTarget() : "");
                targetList.add(targetDto);
            }
            item.setTargetList(targetList);

            List<BaseBodyPartDto> partList = new ArrayList<>();
            List<CourseBodyPart> courseBodyParts = courseBodyPartMapper.selectList(new QueryWrapper<CourseBodyPart>().eq("deleted", "0").eq("courseId", item.getCourseId()));
            for(CourseBodyPart part : courseBodyParts){
                BaseBodyPart baseBodyPart = baseBodyPartMapper.selectById(part.getPartId());
                BaseBodyPartDto partDto = new BaseBodyPartDto();
                partDto.setId(part.getPartId());
                partDto.setName(baseBodyPart!=null ? baseBodyPart.getPart() : "");
                partList.add(partDto);
            }
            item.setPartList(partList);
        });
        tPage.setRecords(userCourseInfoList);
        return ResultUtil.success(tPage);
    }

    @Override
    public Result getUserTrainInfo(String userId, String currentPage) {
        Page<TrainListDto> tPage = new Page<TrainListDto>(Long.parseLong(currentPage), PageInfo.pageSize);
        tPage.setRecords( userInfoMapper.getUserTrainInfo(tPage,userId));
        return ResultUtil.success(tPage);
    }

    @Override
    public UserTrainDataDto getUserTrainData(UserTrainDataQueryRequest userTrainDataQueryRequest) {
        return userInfoMapper.getUserTrainData(userTrainDataQueryRequest);
    }

    @Override
    public List<UserConsecutiveTrainDayDto> getUserTrainConsecutiveDays(UserTrainDataQueryRequest userTrainDataQueryRequest) {
        return userInfoMapper.getUserTrainConsecutiveDays(userTrainDataQueryRequest);
    }

    @Override
    public Result getUserTrainInfoNew(UserTrainInfoQueryRequest userTrainInfoQueryRequest) {
        Page<TrainListDto> tPage = new Page<>(Long.parseLong(userTrainInfoQueryRequest.getCurrentPage()), PageInfo.pageSize);
        List<TrainListDto> userTrainInfoList = userInfoMapper.getUserTrainInfoNew(tPage, userTrainInfoQueryRequest);
        if ("1".equals(userTrainInfoQueryRequest.getTrainType())){
            userTrainInfoList.forEach( item ->{
                //课程类型得查询，需要增加一些信息
                TrainCourseHeadInfo trainCourseHeadInfo = trainCourseHeadInfoMapper.selectById(item.getTrainReportId());
                if (StringUtils.isNotEmpty(trainCourseHeadInfo)){
                    //课程id
                    String courseHeadId = trainCourseHeadInfo.getCourseHeadId();
                    CourseHead courseHead = courseHeadMapper.selectById(courseHeadId);
                    if (StringUtils.isNotEmpty(courseHead)){
                        item.setCourseId(courseHeadId);
                        BaseDifficulty baseDifficulty = baseDifficultyMapper.selectById(courseHead.getDifficultyId());
                        item.setDifficulty(StringUtils.isNotEmpty(baseDifficulty) ? baseDifficulty.getDifficulty() : "");
                        Attach attach = attachMapper.selectById(courseHead.getPicAttachId());
                        item.setPicAttachUrl(attach!=null ? fileImagesPath+attach.getFilePath() : "");
                        List<BaseBodyPartDto> partList = new ArrayList<>();
                        List<CourseBodyPart> courseBodyParts = courseBodyPartMapper.selectList(new QueryWrapper<CourseBodyPart>().eq("deleted", "0").eq("courseId", courseHead.getId()));
                        for (CourseBodyPart part : courseBodyParts){
                            BaseBodyPart baseBodyPart = baseBodyPartMapper.selectById(part.getPartId());
                            if (StringUtils.isNotEmpty(baseBodyPart)){
                                BaseBodyPartDto partDto = new BaseBodyPartDto();
                                partDto.setId(baseBodyPart.getId());
                                partDto.setName(baseBodyPart.getPart());
                                partList.add(partDto);
                            }
                        }
                        item.setPartList(partList);
                    }
                }
            });
        }
        tPage.setRecords(userTrainInfoList);

        return ResultUtil.success(tPage);
    }

    @Override
    public Result getUserTrainCourseList(String userId, String currentPage){
        log.info("-----------------------------------获取用户已训练课程列表开始--------------------------------------");
        log.info("getUserTrainCourseList userId:{{}}",userId);
        List<UserTrainCourseDto> list = new ArrayList<>();
        Page<UserTrainCourseDto> tPage = new Page<>(Long.parseLong(currentPage), PageInfo.pageSize);
        List<UserTrainCourseEntity> userTrainCourseList = userInfoMapper.getUserTrainCourseList(tPage, userId);
        log.info("getUserTrainCourseList userTrainCourseList:{{}}",userTrainCourseList);
        for(UserTrainCourseEntity entity : userTrainCourseList){
            UserTrainCourseDto dto = new UserTrainCourseDto();
            BeanUtils.copyProperties(entity,dto);
            log.info("getUserTrainCourseList dto:{{}}",dto);
            dto.setPicAttachUrl(fileImagesPath+entity.getPicAttachUrl());
            //dto.setCourseType(CourseTypeEnum.values()[Integer.valueOf(entity.getCourseType())].getDisplay());
            dto.setCourseType(entity.getCourseType());
            if(entity.getAmount()!=null&&new BigDecimal(0).compareTo(entity.getAmount())==0){
                dto.setPay(true);
            }else {
                Result check = userPayService.check(userId, UserPayCheckEnum.COURSE.ordinal(), entity.getId());
                dto.setPay((boolean) check.getData());
            }
            log.info("getUserTrainCourseList dto:{{}}",dto);
            Integer trainNum = userCourseMapper.getTrainNum(userId,entity.getId());
            dto.setTrainNum(trainNum);
            dto.setCalorie(entity.getCalorie());
            log.info("getUserTrainCourseList dto:{{}}",dto);
            list.add(dto);
        }
        tPage.setRecords(list);
        log.info("getUserTrainCourseList tPage:{{}}",tPage);
        log.info("-----------------------------------获取用户已训练课程列表结束--------------------------------------");
        return ResultUtil.success(tPage);
    }

    @Override
    public User insertReturnEntity(User entity) {
        return null;
    }

    @Override
    public User updateReturnEntity(User entity) {
        return null;
    }
}