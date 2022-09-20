package com.knd.front.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.page.PageInfo;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.front.domain.SeriesDifficultyEnum;
import com.knd.front.domain.SportEnum;
import com.knd.front.domain.UserPayCheckEnum;
import com.knd.front.entity.*;
import com.knd.front.home.service.IViewRecordService;
import com.knd.front.login.mapper.BaseFrequencyMapper;
import com.knd.front.login.mapper.BaseSportMapper;
import com.knd.front.login.mapper.UserDetailMapper;
import com.knd.front.train.domain.ProgramTypeEnum;
import com.knd.front.train.mapper.TrainProgramMapper;
import com.knd.front.user.dto.*;
import com.knd.front.user.entity.PowerTestRecommendEntity;
import com.knd.front.user.entity.SeriesCourseEntity;
import com.knd.front.user.entity.UserCourseEntity;
import com.knd.front.user.mapper.PowerTestRecommendMapper;
import com.knd.front.user.mapper.ProgramMapper;
import com.knd.front.user.mapper.UserCourseMapper;
import com.knd.front.user.service.IUserPayService;
import com.knd.front.user.service.IUserRecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lenovo
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class UserRecommendServiceImpl implements IUserRecommendService {

    private final IUserPayService userPayService;
    private final ProgramMapper programMapper;
    private final UserCourseMapper userCourseMapper;
    private final TrainProgramMapper trainProgramMapper;
    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    private final UserDetailMapper userDetailMapper;
    private final PowerTestRecommendMapper powerTestRecommendMapper;
    private final BaseFrequencyMapper baseFrequencyMapper;
    private final BaseSportMapper baseSportMapper;
    private IViewRecordService iViewRecordService;

    @Override
    public Result getUserRecommendCourse(String userId, String currentPage, String pageSize) {
        Page<UserCourseEntity> tPage = new Page<>(Long.parseLong(currentPage), StringUtils.isEmpty(pageSize) ? PageInfo.pageSize : Integer.parseInt(pageSize));
        List<UserCourseEntity> courseEntityList = new ArrayList<>();
        PowerTestRecommendEntity recommendEntity = powerTestRecommendMapper.selectOne(new QueryWrapper<PowerTestRecommendEntity>().eq("userId", userId).eq("deleted", "0"));
        if (StringUtils.isNotEmpty(recommendEntity)) {
            //根据测试结果推荐
            String difficultyId = recommendEntity.getRecommendDifficulty();
            courseEntityList = userCourseMapper.getCourseList(tPage, difficultyId);
            log.info("courseEntityList:{{}}" + courseEntityList);
        } else {
            if (StringUtils.isNotEmpty(userId)) {
                UserDetail userDetail = userDetailMapper.selectOne(new QueryWrapper<UserDetail>().eq("deleted", "0").eq("userId", userId));
                BaseSportEntity baseSportEntity = baseSportMapper.selectById(userDetail.getSportId());
                BaseFrequencyEntity baseFrequencyEntity = baseFrequencyMapper.selectById(userDetail.getFrequencyId());

                log.info("userDetail:{{}}" + userDetail);
                String frequencyId = userDetail.getFrequencyId();
                if (StringUtils.isNotEmpty(userDetail) &&
                        StringUtils.isNotEmpty(frequencyId)) {
                    if (SportEnum.AEROBIC.getDisplay().equals(baseSportEntity.getSport()) || StringUtils.isEmpty(userDetail.getSportId())) {
                        //运动方式为空或者是有氧
                        courseEntityList = userCourseMapper.getCourseList(tPage, "1");
                        log.info("courseEntityList:{{}}" + courseEntityList);
                    } else if (SportEnum.ANAEROBIC.getDisplay().equals(baseSportEntity.getSport()) || SportEnum.AEROBICANDANAEROBIC.getDisplay().equals(baseSportEntity.getSport())) {
                        //运动方式为无氧 || 有氧无氧结合
                        switch (baseFrequencyEntity.getFrequency()) {
                            case "1-2次/周":
                                courseEntityList = userCourseMapper.getCourseList(tPage, "2");
                                break;
                            case "3-4次/周":
                                courseEntityList = userCourseMapper.getCourseList(tPage, "3");
                                break;
                            case "5-6次/周":
                                courseEntityList = userCourseMapper.getCourseList(tPage, "4");
                                break;
                            default:
                                courseEntityList = userCourseMapper.getCourseList(tPage, "1");
                                break;
                        }
                    } else {
                        courseEntityList = userCourseMapper.getCourseList(tPage, "1");
                        log.info("courseEntityList:{{}}" + courseEntityList);
                    }
                } else {
                    //只返回基础难度的课程
                    courseEntityList = userCourseMapper.getCourseList(tPage, "1");
                    log.info("courseEntityList:{{}}" + courseEntityList);
                }
            } else {
                //不传userId代表用户未登陆的首页,显示
                courseEntityList = userCourseMapper.getAllCourseList(tPage);
                log.info("courseEntityList:{{}}" + courseEntityList);
            }
        }

        List<UserCoursePayDto> courseList = new ArrayList<>();
        courseEntityList.stream().forEach(course -> {
            UserCoursePayDto courseDto = new UserCoursePayDto();
            BeanUtils.copyProperties(course, courseDto);
            if (StringUtils.isNotEmpty(course.getPicAttachUrl())) {
                courseDto.setPicAttachUrl(fileImagesPath + course.getPicAttachUrl());
            }
            //courseDto.setCourseType(CourseTypeEnum.values()[Integer.valueOf(course.getCourseType())].getDisplay());
            courseDto.setCourseType(course.getCourseType());
            if (new BigDecimal(0).compareTo(course.getAmount()) == 0) {
                courseDto.setPay(true);
            } else {
                if (StringUtils.isNotEmpty(userId)) {
                    Result check = userPayService.check(userId, UserPayCheckEnum.COURSE.ordinal(), course.getId());
                    courseDto.setPay((boolean) check.getData());
                } else {
                    courseDto.setPay(false);
                }
            }
            courseList.add(courseDto);
        });
        Page<UserCoursePayDto> page = new Page<>(Long.parseLong(currentPage), PageInfo.pageSize);
        page.setRecords(courseList);
        page.setTotal(tPage.getTotal());
        return ResultUtil.success(page);
    }

    @Override
    public Result getUserRecommendTrain(String userId, String currentPage) {
        Page<TrainProgramDto> tPage = new Page<>(Long.parseLong(currentPage), PageInfo.pageSize);
        List<TrainProgramDto> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(userId)) {
            list = userCourseMapper.getTrainByUserList(tPage, userId);
        }
        if (list.size() == 0) {
            list = userCourseMapper.getTrainAllList(tPage);
        }
        list.stream().forEach(l -> {
            if (StringUtils.isNotEmpty(l.getPicAttachUrl())) {
                l.setPicAttachUrl(fileImagesPath + l.getPicAttachUrl());
            }
        });
        tPage.setRecords(list);
        return ResultUtil.success(tPage);
    }

    @Override
    public Result getTypeTrain(ProgramTypeEnum type, String currentPage) {
        Page<TrainProgramDto> tPage = new Page<>(Long.parseLong(currentPage), PageInfo.pageSize);
        List<TrainProgramDto> list = userCourseMapper.getTrainByTypeList(tPage, String.valueOf(type.ordinal()));
        list.stream().forEach(l -> {
            if (StringUtils.isNotEmpty(l.getPicAttachUrl())) {
                l.setPicAttachUrl(fileImagesPath + l.getPicAttachUrl());
            }
        });
        tPage.setRecords(list);
        return ResultUtil.success(tPage);
    }

    @Override
    public Result getUserRecommendTrainDetail(String userId, String id) {
        TrainProgramDetailDto dto = new TrainProgramDetailDto();
        ProgramEntity programEntity = programMapper.selectById(id);
        List<TrainProgramWeekDetailDto> trainProgramWeekDetail = new ArrayList<>();
        List<ProgramWeekDetailEntity> weekList = trainProgramMapper.getWeekList(id);
        for (ProgramWeekDetailEntity week : weekList) {
            List<UserCoursePayDto> coursePayDtoList = new ArrayList<>();
            List<UserCourseEntity> dayList = trainProgramMapper.getCourseList(week.getId());
            dayList.stream().forEach(day -> {
                UserCoursePayDto userCoursePayDto = new UserCoursePayDto();
                BeanUtils.copyProperties(day, userCoursePayDto);
                //userCoursePayDto.setCourseType(CourseTypeEnum.values()[Integer.valueOf(day.getCourseType())].getDisplay());
                userCoursePayDto.setCourseType(day.getCourseType());
                if (new BigDecimal(0).compareTo(day.getAmount()) == 0) {
                    userCoursePayDto.setPay(true);
                } else {
                    if (StringUtils.isNotEmpty(userId)) {
                        Result check = userPayService.check(userId, UserPayCheckEnum.COURSE.ordinal(), day.getId());
                        userCoursePayDto.setPay((boolean) check.getData());
                    } else {
                        userCoursePayDto.setPay(false);
                    }
                }
                if (StringUtils.isNotEmpty(userCoursePayDto.getPicAttachUrl())) {
                    userCoursePayDto.setPicAttachUrl(fileImagesPath + userCoursePayDto.getPicAttachUrl());
                }
                coursePayDtoList.add(userCoursePayDto);
            });
            trainProgramWeekDetail.add(TrainProgramWeekDetailDto.builder()
                    .weekDayName(week.getWeekDayName())
                    .trainCourseList(coursePayDtoList).build());
        }
        dto.setType(programEntity.getType());
        dto.setProgramName(programEntity.getProgramName());
        dto.setTrainWeekNum(programEntity.getTrainWeekNum());
        dto.setTrainProgramWeekDetail(trainProgramWeekDetail);
        //iViewRecordService.addViewRecord(ViewTypeEnum.TRAIN_PROGRAM.getCode(),id);
        return ResultUtil.success(dto);
    }

    @Override
    public Result getUserSeriesCourseList(String userId, String currentPage) {
        List<UserSeriesCourseListDto> resList = new ArrayList<>();
        Page<SeriesCourseEntity> page = new Page<>(Long.parseLong(currentPage), PageInfo.pageSize);
        List<SeriesCourseEntity> seriesList = new ArrayList<>();
        if (StringUtils.isNotEmpty(userId)) {
            seriesList = userCourseMapper.getSeriesCourseList(page, userId);
        }
        if (seriesList.size() == 0) {
            seriesList = userCourseMapper.getSeriesCourseAllList(page);
        }
        for (SeriesCourseEntity series : seriesList) {
            UserSeriesCourseListDto seriesCourseDto = new UserSeriesCourseListDto();
            BeanUtils.copyProperties(series, seriesCourseDto);
            seriesCourseDto.setPicAttachUrl(fileImagesPath + series.getFilePath());
            resList.add(seriesCourseDto);
        }
        Page<UserSeriesCourseListDto> tPage = new Page<>(Long.parseLong(currentPage), PageInfo.pageSize);
        tPage.setTotal(page.getTotal());
        tPage.setRecords(resList);
        return ResultUtil.success(tPage);
    }

    @Override
    public Result getUserSeriesCourseDetail(String userId, String id) {
        UserSeriesCourseDto dto = new UserSeriesCourseDto();
        SeriesCourseEntity series = userCourseMapper.getSeriesCourseById(id);
        if (StringUtils.isEmpty(series)) {
            return ResultUtil.error(ResultEnum.FAIL);
        }
        BeanUtils.copyProperties(series, dto);
        dto.setDifficulty(SeriesDifficultyEnum.values()[Integer.valueOf(series.getDifficulty())].getDisplay());
        dto.setPicAttachUrl(fileImagesPath + series.getFilePath());
        List<String> attachList = userCourseMapper.getAttachList(series.getId());
        Collections.sort(attachList);
        List<String> attachUrl = new ArrayList<>();
        for (String filePath : attachList) {
            attachUrl.add(fileImagesPath + filePath);
        }
        dto.setAttachUrl(attachUrl);

        //课程
        List<UserCoursePayDto> coursePayDtoList = new ArrayList<>();
        List<String> courseIdList = new ArrayList<>();
        List<UserCourseEntity> courseList = userCourseMapper.getCourseEntityList(series.getId());
        for (UserCourseEntity entity : courseList) {
            courseIdList.add(entity.getId());
            UserCoursePayDto userCoursePayDto = new UserCoursePayDto();
            BeanUtils.copyProperties(entity, userCoursePayDto);
            if (StringUtils.isNotEmpty(userCoursePayDto.getPicAttachUrl())) {
                userCoursePayDto.setPicAttachUrl(fileImagesPath + userCoursePayDto.getPicAttachUrl());
            }
            //userCoursePayDto.setCourseType(CourseTypeEnum.values()[Integer.valueOf(entity.getCourseType())].getDisplay());
            userCoursePayDto.setCourseType(entity.getCourseType());
            if (new BigDecimal(0).compareTo(entity.getAmount()) == 0) {
                userCoursePayDto.setPay(true);
            } else {
                if (StringUtils.isNotEmpty(userId)) {
                    Result check = userPayService.check(userId, UserPayCheckEnum.COURSE.ordinal(), entity.getId());
                    userCoursePayDto.setPay((boolean) check.getData());
                } else {
                    userCoursePayDto.setPay(false);
                }
            }
            if (StringUtils.isNotEmpty(userId)) {
                Integer trainNum = userCourseMapper.getTrainNum(userId, entity.getId());
                userCoursePayDto.setTrainNum(trainNum);
            } else {
                userCoursePayDto.setTrainNum(0);
            }
            coursePayDtoList.add(userCoursePayDto);
        }
        dto.setCoursePayDtoList(coursePayDtoList);

        QueryWrapper<CourseTypeNumDto> wrapper = new QueryWrapper<>();
        wrapper.eq("a.deleted", "0");
        wrapper.eq("a.releaseFlag", "1");
        if (courseIdList.size() > 0) {
            wrapper.in("a.id", courseIdList);
        }
        wrapper.groupBy("c.id");
        List<CourseTypeNumDto> courseTypeNumList = userCourseMapper.getCourseTypeNumList(wrapper);
        dto.setCourseTypeNumList(courseTypeNumList);
        //保存浏览记录
        //iViewRecordService.addViewRecord(ViewTypeEnum.SERIES_COURSE.getCode(),id);
        return ResultUtil.success(dto);
    }

    @Override
    public Result getUserRecommendCourseAndHotCourse(String userId, String courseType, String currentPage, String pageSize) {
        Page<UserCourseEntity> tPage = new Page<>(Long.parseLong(currentPage), StringUtils.isEmpty(pageSize) ? PageInfo.pageSize : Integer.parseInt(pageSize));
        List<UserCourseEntity> courseEntityList = new ArrayList<>();
        if (StringUtils.isNotEmpty(userId)) {
                    courseEntityList = userCourseMapper.getCourseListByCourseType(tPage, "%"+courseType+"%");
                    log.info("courseEntityList:{{}}" + courseEntityList);
        } else {
            //不传userId代表用户未登陆的首页,显示全部课程
            courseEntityList = userCourseMapper.getCourseListAll(tPage);
            log.info("courseEntityList:{{}}" + courseEntityList);
        }
        for (UserCourseEntity courseEntity: courseEntityList) {
            courseEntity.setPicAttachUrl(fileImagesPath+courseEntity.getPicAttachUrl());

        }
        Page<UserCourseEntity> page = new Page<>(Long.parseLong(currentPage), PageInfo.pageSize);
        page.setRecords(courseEntityList);
        page.setTotal(tPage.getTotal());
        return ResultUtil.success(page);
    }


}
