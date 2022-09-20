package com.knd.front.live.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.front.live.dto.*;
import com.knd.front.live.request.CoachRequest;
import com.knd.front.live.request.SaveCoachCourseRequest;
import com.knd.front.live.service.CoachService;
import com.knd.front.live.service.IRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/front/live")
@Slf4j
@Api(tags = "教练信息")
@RequiredArgsConstructor
public class CoachController {
    private final CoachService coachService;

    private final IRoomService iRoomService;

    @Log("获取全部教练列表")
    @ApiOperation(value = "获取全部教练列表",notes = "获取全部教练列表")
    @PostMapping("/getCoachList")
    public Result<Page<CoachListDto>> getCoachList(@RequestBody @Validated CoachRequest request, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return coachService.getCoachList(request);
    }

    @Log("获取我的私教列表")
    @ApiOperation(value = "获取我的私教列表",notes = "获取我的私教列表")
    @GetMapping("/getCoachByUser")
    public Result<CoachListByUserDto> getCoachByUser(@ApiParam("用户id") @RequestParam(required = true) String userId){
        return coachService.getCoachByUser(userId);
    }

    @Log("获取教练详情")
    @ApiOperation(value = "获取教练详情",notes = "获取教练详情")
    @GetMapping("/getCoachDetails")
    public Result<CoachDetailsDto> getCoachDetails(@ApiParam("教练id") @RequestParam(required = true) String userId,
                                                   @ApiParam("教练用户id") @RequestParam(required = true) String coachUserId){
        return coachService.getCoachDetails(userId,coachUserId);
    }


    @Log("获取用户已预约教练课程列表")
    @ApiOperation(value = "获取用户已预约教练课程列表",notes = "获取用户已预约教练课程列表")
    @GetMapping("/getCoachCourseByUserList")
    public Result<CoachCourseByUserListDto> getCoachCourseByUserList(@ApiParam(value = "用户id",required = true) @RequestParam(required = true) String userId,
                                                                     @ApiParam(value = "教练用户id") @RequestParam(required = false) String coachUserId,
                                                                     @ApiParam(value = "课程类别0课前咨询 1私教课程 2团课") @RequestParam(required = false) List<String> typeList,
                                                                     @ApiParam("当前页")  @RequestParam(required = false) String current){
        return coachService.getCoachCourseByUserList(userId,coachUserId,typeList,current);
    }

    @Log("日历（用户查看教练课程）")
    @ApiOperation(value = "日历（用户查看教练课程）",notes = "日历（用户查看教练课程）")
    @GetMapping("/getCoachOrderList")
    public Result<CoachCourseOrderListDto> getCoachOrderList(@ApiParam("用户id") @RequestParam(required = true) String userId,
                                                             @ApiParam("开始日期") @RequestParam(required = true) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate beginDate,
                                                             @ApiParam("结束日期") @RequestParam(required = true) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate,
                                                             @ApiParam("教练用户id") @RequestParam(required = false) String coachUserId,
                                                             @ApiParam("课程类别0课前咨询 1私教课程 2团课") @RequestParam(required = true) List<String> typeList){
        return coachService.getCoachOrderList(userId,typeList,coachUserId,beginDate,endDate);
    }

    @Log("日历（教练查看课程）")
    @ApiOperation(value = "日历（教练查看课程）",notes = "日历（教练查看课程）")
    @GetMapping("/getDayOrderList")
    public Result<DayOrderListDto> getDayOrderList(@ApiParam("开始日期") @RequestParam(required = true) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate beginDate,
                                                   @ApiParam("结束日期") @RequestParam(required = true) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate,
                                                   @ApiParam("教练用户id") @RequestParam(required = true) String userId){
        return coachService.getDayOrderList(userId,beginDate,endDate);
    }

    @Log("获取教练课程详情")
    @ApiOperation(value = "获取教练课程详情",notes = "获取教练课程详情")
    @GetMapping("/getCoachCourseById")
    public Result<CoachCourseOrderListDto> getCoachCourseById(@ApiParam("教练课程时间id") @RequestParam(required = true) String timeId,
                                                              @ApiParam("用户id") @RequestParam(required = true) String userId){
        return coachService.getCoachCourseById(timeId,userId);
    }

    @Log("获取往期直播或者未来直播列表或者今日回放或者全部课程")
    @ApiOperation(value = "获取往期直播或者未来直播列表或者今日回放",notes = "获取往期直播或者未来直播列表或者今日回放")
    @GetMapping("/getLiveList")
    public Result<Page<LiveListDto>> getLiveList(@ApiParam("查询类型：0往期直播 1未来直播 2今日回放 传多个为全部") @RequestParam(required = true, name = "") String type,
                                                 @ApiParam("课程类型：0课前咨询 1私教课程 2团课直播 不传为全部") @RequestParam(required = false, name = "") String courseType,
                                                 @ApiParam("用户id") @RequestParam(required = true) String userId,
                                                 @ApiParam("教练用户id") @RequestParam(required = false) String coachUserId,
                                                 @ApiParam("当前页")  @RequestParam(required = false) String current){
        return coachService.getLiveList(userId,current,type,coachUserId,courseType);
    }

    @Log("获取正在直播")
    @ApiOperation(value = "获取正在直播",notes = "获取正在直播")
    @GetMapping("/getLiveIngList")
    public Result<Page<LiveIngListDto>> getLiveIngList(@ApiParam("用户id") @RequestParam(required = true) String userId,
                                                       @ApiParam("当前页") @RequestParam(required = false) String current){
        return coachService.getLiveIngList(userId,current);
    }


    @Log("教练直播计划列表")
    @ApiOperation(value = "教练直播计划列表",notes = "教练直播计划列表")
    @GetMapping("/queryCoachCourseTime")
    public Result<Page<CoachCourseTimeDto>> queryCoachCourseTime(
            @ApiParam("当前页") @RequestParam(required = true) String current,
            @ApiParam("直播课状态分类: 0-查询所有，1-最新预约 2-待上，3-已完成") @RequestParam(required = true) String status){
        return coachService.queryCoachCourseTime(current,status);
    }

    @Log("新增排课")
    @ApiOperation("新增排课")
    @PostMapping("/publishCourse")
    public Result publishCourse(@RequestBody @Validated SaveCoachCourseRequest request, BindingResult bindingResult){
        //userId从token获取
        String userId = UserUtils.getUserId();
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return coachService.publishCourse(userId,request);
    }

    @Log("私教关闭房间")
    @PostMapping("/closeRoom")
    @ApiOperation(value = "关闭房间 私教关闭房间", notes = "关闭房间 私教关闭房间")
    public Result coachcloseRoom(@ApiParam("主键timeId") @RequestParam(required = true, name = "id") String id) {
        return iRoomService.coachcloseRoom(id);
    }

}
