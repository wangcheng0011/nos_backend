package com.knd.front.home.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.DateUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.entity.IntroductionCourse;
import com.knd.front.home.dto.IntroductionCourseIdDto;
import com.knd.front.home.mapper.IntroductionCourseMapper;
import com.knd.front.home.request.FinishWatchCourseVideoRequest;
import com.knd.front.home.request.WatchCourseVideoRequest;
import com.knd.front.home.service.IIntroductionCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-07-02
 */
@Service
public class IntroductionCourseServiceImpl extends ServiceImpl<IntroductionCourseMapper, IntroductionCourse> implements IIntroductionCourseService {
    @Autowired
    private IntroductionCourseMapper introductionCourseMapper;

    @Override
    public IntroductionCourse insertReturnEntity(IntroductionCourse entity) {
        return null;
    }

    @Override
    public IntroductionCourse updateReturnEntity(IntroductionCourse entity) {
        return null;
    }

    @Override
    public Result watchCourseVideo(WatchCourseVideoRequest watchCourseVideoRequest) {
        IntroductionCourse introductionCourse = new IntroductionCourse();
        introductionCourse.setUserId(watchCourseVideoRequest.getUserId());
        introductionCourse.setCourse(watchCourseVideoRequest.getCourse());
        introductionCourse.setCourseHeadId(watchCourseVideoRequest.getCourseId());
        introductionCourse.setVedioBeginTime(DateUtils.getCurrentDateTimeStr());
        introductionCourse.setEquipmentNo(watchCourseVideoRequest.getEquipmentNo());
        introductionCourseMapper.insert(introductionCourse);
        IntroductionCourseIdDto introductionCourseIdDto = new IntroductionCourseIdDto();
        introductionCourseIdDto.setIntroductionCourseId(introductionCourse.getId());
        return ResultUtil.success(introductionCourseIdDto);
    }

    @Override
    public Result finishWatchCourseVideo(FinishWatchCourseVideoRequest finishWatchCourseVideoRequest) {
       introductionCourseMapper.updateByUserIdAndCourseId(finishWatchCourseVideoRequest, DateUtils.getCurrentDateTimeStr());
       return ResultUtil.success();
    }
}
