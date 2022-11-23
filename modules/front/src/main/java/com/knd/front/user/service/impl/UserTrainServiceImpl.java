package com.knd.front.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.StringUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.domain.RankingTypeEnum;
import com.knd.front.entity.TrainUserPraiseEntity;
import com.knd.front.user.dto.RankingListDto;
import com.knd.front.user.dto.UserTrainDto;
import com.knd.front.user.mapper.UserTrainByUserMapper;
import com.knd.front.user.mapper.UserTrainMapper;
import com.knd.front.user.mapper.UserTrainPraiseMapper;
import com.knd.front.user.request.UserTrainPraiseRequest;
import com.knd.front.user.service.UserTrainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * @author zm
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserTrainServiceImpl implements UserTrainService {
    private final UserTrainMapper userTrainMapper;
    private final UserTrainPraiseMapper userTrainPraiseMapper;
    private final UserTrainByUserMapper userTrainByUserMapper;
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;

    @Override
    public Result getRankingList(RankingTypeEnum type,LocalDate date,String userId,Long size) {
        log.info("-------------------------------我要获取排行榜啦--------------------------------");
        log.info("getRankingList type:{{}}",type);
        log.info("getRankingList date:{{}}",date);
        log.info("getRankingList userId:{{}}",userId);
        log.info("getRankingList size:{{}}",size);
        if(date == null){
            date = LocalDate.now();
        }
        //本月开始时间
       /* LocalDate firstDay = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime month_start = LocalDateTime.of(firstDay, LocalTime.MIN);
        //本月结束时间
        LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime month_end = LocalDateTime.of(lastDay, LocalTime.MAX);*/
        List<UserTrainDto> userTrainList;
        UserTrainDto userTrainDto;

        //毅力得字段存在不一样，所以无法统一使用
        if(type.equals(RankingTypeEnum.WILL)){
            userTrainList = userTrainMapper.getUserWill(userId,date,size);
            log.info("getRankingList userTrainList:{{}}",userTrainList);
            for(UserTrainDto dto : userTrainList){
                if(StringUtils.isEmpty(dto.getTrainNum())){
                    dto.setTrainNum("0");
                }
                if(StringUtils.isNotEmpty(dto.getHeadPicUrl())){
                    dto.setHeadPicUrl(fileImagesPath+dto.getHeadPicUrl());
                }
            }
            userTrainDto = userTrainByUserMapper.getUserWill(userId,date);
            log.info("getRankingList userTrainDto:{{}}",userTrainDto);
            userTrainDto = StringUtils.isNotEmpty(userTrainDto) ? userTrainDto : new UserTrainDto();
            if(StringUtils.isNotEmpty(userTrainDto.getHeadPicUrl())){
                userTrainDto.setHeadPicUrl(fileImagesPath+userTrainDto.getHeadPicUrl());
            }
        }else{
            QueryWrapper<UserTrainDto> wrapper = new QueryWrapper<>();
            wrapper.eq("u.deleted","0");
            wrapper.groupBy("u.id");
//            wrapper.orderBy(true,false,"trainNum","praiseNum");
            wrapper.orderByDesc("trainNum","praiseNum");
            wrapper.last("limit "+size);
            userTrainList = userTrainMapper.getUserTrainByParam(userId,date,type.getParamType(),type.getPraiseType(),wrapper);
            log.info("getRankingList userTrainList:{{}}",userTrainList);
            for(UserTrainDto dto : userTrainList){
                if(StringUtils.isEmpty(dto.getTrainNum())){
                    dto.setTrainNum("0");
                }
                if(StringUtils.isNotEmpty(dto.getHeadPicUrl())){
                    dto.setHeadPicUrl(fileImagesPath+dto.getHeadPicUrl());
                }
            }
            wrapper.clear();
            wrapper.eq("u.id",userId);
            List<UserTrainDto> userTrainListByUser = userTrainMapper.getUserTrainByParam(userId,date,type.getParamType(),type.getPraiseType(),wrapper);
            log.info("getRankingList userTrainListByUser:{{}}",userTrainListByUser);
            userTrainDto = StringUtils.isNotEmpty(userTrainListByUser) ? userTrainListByUser.get(0) : new UserTrainDto();
            log.info("getRankingList userTrainDto:{{}}",userTrainDto);
            if(StringUtils.isNotEmpty(userTrainDto.getHeadPicUrl())){
                userTrainDto.setHeadPicUrl(fileImagesPath+userTrainDto.getHeadPicUrl());
            }
        }
        RankingListDto dto = RankingListDto.builder().userTrainDto(userTrainDto).userTrainList(userTrainList).build();
        log.info("-----------------------------获取排行榜结束---------------------------------------");
        return ResultUtil.success(dto);
    }

    @Override
    public Result praise(UserTrainPraiseRequest userTrainPraiseRequest) {
        LocalDate date = LocalDate.now();
        //本月开始时间
        LocalDate firstday = date.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime month_start = LocalDateTime.of(firstday, LocalTime.MIN);
        //本月结束时间
        LocalDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime month_end = LocalDateTime.of(lastDay, LocalTime.MAX);

        TrainUserPraiseEntity entity = userTrainPraiseMapper.getPraise(userTrainPraiseRequest.getUserId()
                , userTrainPraiseRequest.getPraiseUserId()
                , String.valueOf(userTrainPraiseRequest.getPraiseType().ordinal())
                , month_start,month_end);
        if(entity == null){
            TrainUserPraiseEntity trainUserPraiseEntity = new TrainUserPraiseEntity();
            BeanUtils.copyProperties(userTrainPraiseRequest,trainUserPraiseEntity);
            trainUserPraiseEntity.setPraiseType(String.valueOf(userTrainPraiseRequest.getPraiseType().ordinal()));
            userTrainPraiseMapper.insert(trainUserPraiseEntity);
        }else{
            String praise = entity.getPraise();
            //点赞
            if("0".equals(praise)){
                entity.setPraise("1");
            }else  if("1".equals(praise)){//已取消
                entity.setPraise("0");
            }
            userTrainPraiseMapper.updateById(entity);
        }
        return ResultUtil.success();
    }
}
