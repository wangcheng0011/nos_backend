package com.knd.front.train.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.front.train.service.ICourseDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/6
 * @Version 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("/front/train")
@Slf4j
@Api(tags = "train")
public class CourseDetailController {
    @Autowired
    private ICourseDetailService iCourseDetailService;

    @ApiOperation(value = "I062-获取视频详情页",notes = "I062-获取视频详情页")
    @Log("I062-获取视频详情页")
    @GetMapping("/getCourseDetail")
    public Result getCourseDetail(@RequestParam String courseId,@RequestParam(required = false) String userId){
        return iCourseDetailService.getCourseDetail(courseId,userId);
    }

    @ApiOperation(value = "I063-获取视频小节信息",notes = "I063-获取视频小节信息")
    @Log("I063-获取视频小节信息")
    @GetMapping("/getCourseNodeDetail")
    public Result getCourseNodeDetail(@RequestParam String courseId,@RequestParam(required = false) String userId){
        return iCourseDetailService.getCourseNodeDetail(courseId,userId);
    }

    @GetMapping("/getCourseVideoProgressInfo")
    @Log("I064-获取视频进度条信息")
    @ApiOperation(value = "I064-获取视频进度条信息",notes = "I064-获取视频进度条信息")
    public Result getCourseVideoProgressInfo(@RequestParam String courseId,@RequestParam(required = false) String userId){
        return iCourseDetailService.getCourseVideoProgressInfo(courseId,userId);
    }

}