package com.knd.front.home.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.home.request.FinishWatchCourseVideoRequest;
import com.knd.front.home.request.UserQueryCourseRequest;
import com.knd.front.home.request.WatchCourseVideoRequest;
import com.knd.front.home.service.ICourseHeadService;
import com.knd.front.home.service.IIntroductionCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("/front/home")
@Slf4j
@Api(tags = "home")
@RequiredArgsConstructor
public class CourseController {
    private final IIntroductionCourseService iIntroductionCourseService;
    private final ICourseHeadService courseHeadService;



    @PostMapping("/watchCourseVideo")
    @Log("I051-创建观看账号")
    @ApiOperation(value = "I051-创建观看账号",notes = "I051-创建观看账号")
    public Result watchCourseVideo(@RequestBody @Valid WatchCourseVideoRequest watchCourseVideoRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
            return iIntroductionCourseService.watchCourseVideo(watchCourseVideoRequest);
    }
    @Log("I052-结束观看课程视频")
    @ApiOperation(value = "I052-结束观看课程视频",notes = "I052-结束观看课程视频")
    @PostMapping("/finishWatchCourseVideo")
    public Result finishWatchCourseVideo(@RequestBody @Valid FinishWatchCourseVideoRequest finishWatchCourseVideoRequest,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iIntroductionCourseService.finishWatchCourseVideo(finishWatchCourseVideoRequest);
    }

    @Log("筛选课程")
    @ApiOperation(value = "筛选课程",notes = "筛选课程")
    @GetMapping("/getCourse")
    public Result getCourse(UserQueryCourseRequest request){
        return courseHeadService.getCourse(request);
    }

    @Log("体验课程列表")
    @ApiOperation(value = "体验课程列表",notes = "体验课程列表")
    @GetMapping("/getCoursePage")
    public Result getCoursePage(UserQueryCourseRequest request){
        return courseHeadService.getCoursePage(request);
    }


}