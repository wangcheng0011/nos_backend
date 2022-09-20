package com.knd.front.user.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.front.train.domain.ProgramTypeEnum;
import com.knd.front.user.service.IUserRecommendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/3
 * @Version 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("/front/user")
@Slf4j
@Api(tags = "UserRecommend")
@RequiredArgsConstructor
public class UserRecommendController {
    private final IUserRecommendService iUserRecommendService;

    @Log("I111-获取用户推荐课程列表")
    @ApiOperation(value = "I111-获取用户推荐课程列表",notes = "I111-获取用户推荐课程列表")
    @GetMapping("/getUserRecommendCourse")
    public Result getUserRecommendCourse(@RequestParam String userId,
                                         @RequestParam(required = true) String currentPage,
                                         @RequestParam(required = false) String pageSize){
        return iUserRecommendService.getUserRecommendCourse(userId,currentPage,pageSize);
    }

    @Log("I111-获取用户推荐课程和热门课程列表")
    @ApiOperation(value = "I111-获取用户推荐课程和热门课程",notes = "I111-获取用户推荐课程和热门课程列表")
    @GetMapping("/getUserRecommendCourseAndHotCourse")
    public Result getUserRecommendCourseAndHotCourse(@RequestParam(required = false) String userId,
                                         @RequestParam(required = true) String courseType,
                                         @RequestParam(required = true) String currentPage,
                                         @RequestParam(required = false) String pageSize){
        return iUserRecommendService.getUserRecommendCourseAndHotCourse(userId,courseType,currentPage,pageSize);
    }


    @Log("I111-获取用户推荐训练计划列表")
    @ApiOperation(value = "I111-获取用户推荐训练计划列表",notes = "I111-获取用户推荐训练计划列表")
    @GetMapping("/getUserRecommendTrain")
    public Result getUserRecommendTrain(@RequestParam String userId,
                                        @RequestParam(required = true) String currentPage){
        return iUserRecommendService.getUserRecommendTrain(userId,currentPage);
    }

    @Log("I111-获取各类型推荐训练计划列表")
    @ApiOperation(value = "I111-获取各类型推荐训练计划列表",notes = "I111-获取各类型推荐训练计划列表")
    @GetMapping("/getTypeTrain")
    public Result getTypeTrain(@RequestParam ProgramTypeEnum type, @RequestParam(required = true) String currentPage){
        return iUserRecommendService.getTypeTrain(type,currentPage);
    }

    @Log("I111-获取用户推荐训练计划详情")
    @ApiOperation(value = "I111-获取用户推荐训练计划详情",notes = "I111-获取用户推荐训练计划详情")
    @GetMapping("/getUserRecommendTrainDetail")
    public Result getUserRecommendTrainDetail (@RequestParam String userId,@RequestParam(required = true) String id){
        return iUserRecommendService.getUserRecommendTrainDetail(userId,id);
    }

    @Log("获取用户系列课程推荐列表")
    @ApiOperation(value = "I111-获取用户系列课程推荐列表",notes = "I111-获取用户系列课程推荐列表")
    @GetMapping("/getUserSeriesCourseList")
    public Result getUserSeriesCourseList(@RequestParam String userId,
//                                          @ApiParam("类型：1特色") @RequestParam(required = false) String type,
                                          @RequestParam(required = true) String currentPage){
        return iUserRecommendService.getUserSeriesCourseList(userId, currentPage);
    }

    @Log("I111-获取用户系列课程推荐详情")
    @ApiOperation(value = "I111-获取用户系列课程推荐详情",notes = "I111-获取用户系列课程推荐详情")
    @GetMapping("/getUserSeriesCourseDetail")
    public Result getUserSeriesCourseDetail (@RequestParam String userId,@RequestParam(required = true) String id){
        return iUserRecommendService.getUserSeriesCourseDetail(userId,id);
    }
}
