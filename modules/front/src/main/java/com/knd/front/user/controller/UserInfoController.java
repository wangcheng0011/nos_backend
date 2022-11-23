package com.knd.front.user.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.front.user.dto.*;
import com.knd.front.user.request.UserHealthRequest;
import com.knd.front.user.request.UserTrainDataQueryRequest;
import com.knd.front.user.request.UserTrainInfoQueryRequest;
import com.knd.front.user.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
@Api(tags = "user")
@RequiredArgsConstructor
public class UserInfoController {
    private final IUserInfoService iUserInfoService;
    private final IUserTrainLevelService iUserTrainLevelService;
    private final IUserPowerLevelTestService iUserPowerLevelTestService;
    private final IUserHealthService userHealthService;
    private final IUserActionPowerTestService iUserActionPowerTestService;


    @ApiOperation(value = "I110-获取个人中心综合信息", notes = "I110-获取个人中心综合信息")
    @Log("I110-获取个人中心综合信息")
    @GetMapping("/getUserCenterInfo")
    public Result getUserCenterInfo(@RequestParam @NonNull String userId) {
        return iUserInfoService.getUserCenterInfo(userId);
    }

    @ApiOperation(value = "I11x-获取用户训练数据", notes = "I11x-获取用户训练数据")
    @Log("I11x-运动数据 获取用户训练数据")
    @PostMapping("/getUserTrainData")
    public Result getUserTrainData(@RequestBody @Validated UserTrainDataQueryRequest userTrainDataQueryRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        log.info("getUserTrainData userTrainDataQueryRequest:{{}}",userTrainDataQueryRequest);
        UserTrainDataDto userTrainData = iUserInfoService.getUserTrainData(userTrainDataQueryRequest);
        if(userTrainData == null) {
            userTrainData = new UserTrainDataDto();
        }
        log.info("getUserTrainData userTrainData:{{}}",userTrainData);
        List<UserConsecutiveTrainDayDto> userConsecutiveTrainDayDtos = iUserInfoService.getUserTrainConsecutiveDays(userTrainDataQueryRequest);
        log.info("getUserTrainData userConsecutiveTrainDayDtos:{{}}",userConsecutiveTrainDayDtos);
        if(userConsecutiveTrainDayDtos != null && userConsecutiveTrainDayDtos.size()>0) {
            List<Integer> consecutiveDays = userConsecutiveTrainDayDtos.stream().map(i -> Integer.parseInt(i.getConsecutiveDays())).collect(Collectors.toList());
            int temp =0;
            for(int i=0;i<consecutiveDays.size();i++){
                if(i == 0){
                    temp = consecutiveDays.get(i);
                }else{
                    if(consecutiveDays.get(i)>consecutiveDays.get(i-1)) {
                        temp = consecutiveDays.get(i);
                    }
                }
            }
            userTrainData.setMaxConsecutiveDays(temp+"");
            //userTrainData.setMaxConsecutiveDays(Collections.max(consecutiveDays)+"");
            userTrainData.setCurrentConsecutiveDays(userConsecutiveTrainDayDtos.get(userConsecutiveTrainDayDtos.size()-1).getConsecutiveDays());
            log.info("getUserTrainData CurrentConsecutiveDays:{{}}",userConsecutiveTrainDayDtos.get(userConsecutiveTrainDayDtos.size()-1).getConsecutiveDays());
        }else{
            userTrainData.setMaxConsecutiveDays("0");
            //userTrainData.setMaxConsecutiveDays(Collections.max(consecutiveDays)+"");
            userTrainData.setCurrentConsecutiveDays("0");
        }
        log.info("getUserTrainData userTrainData:{{}}",userTrainData);
        return ResultUtil.success(userTrainData);

    }

    @Log("I111-获取用户课程列表")
    @ApiOperation(value = "I111-获取用户课程列表",notes = "I111-获取用户课程列表")
    @GetMapping("/getUserCourseInfo")
    public Result getUserCourseInfo(@RequestParam String userId,@RequestParam String currentPage){
        return iUserInfoService.getUserCourseInfo(userId,currentPage);
    }

    @Log("I112-获取用户运动列表")
    @ApiOperation(value = "I112-获取用户运动列表", notes = "I112-获取用户运动列表")
    @GetMapping("/getUserTrainInfo")
    public Result getUserTrainInfo(@RequestParam @NonNull String userId, @RequestParam @NonNull String currentPage) {

        return iUserInfoService.getUserTrainInfo(userId,currentPage);
    }

    @Log("I11X-获取用户运动数据列表")
    @ApiOperation(value = "I11X-获取用户运动数据列表", notes = "I11X-获取用户运动数据列表")
    @PostMapping("/getUserTrainInfoNew")
    public Result getUserTrainInfoNew(@RequestBody @Validated UserTrainInfoQueryRequest userTrainInfoQueryRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error =  bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iUserInfoService.getUserTrainInfoNew(userTrainInfoQueryRequest);
    }

    @Log("I113-获取用户运动详情(课程训练）")
    @ApiOperation(value = "I113-获取用户运动详情(课程训练）", notes = "I113-获取用户运动详情(课程训练）")
    @GetMapping("/getUserTrainCourseDetail")
    public Result getUserTrainCourseDetail(@RequestParam @NotBlank String userId, @RequestParam @NotBlank String trainReportId) {
        return iUserInfoService.getUserTrainCourseDetail(userId, trainReportId);
    }

    @Log("I114-获取用户运动详情（动作) ")
    @ApiOperation(value = "I114-获取用户运动详情（动作) ", notes = "I114-获取用户运动详情（动作) ")
    @GetMapping("/getUserTrainFreeDetail")
    public Result getUserTrainFreeDetail(@RequestParam @NonNull String userId, @RequestParam @NonNull String trainReportId) {
        return iUserInfoService.getUserTrainFreeDetail(userId,trainReportId);
    }
    @Log("I115-获取用户训练等级列表")
    @ApiOperation(value = "I115-获取用户训练等级列表",notes = "I115-获取用户训练等级列表")
    @GetMapping("/getUserTrainLevels")
    public Result getUserTrainLevels(@RequestParam @NonNull String userId,@RequestParam String currentPage){

        return iUserTrainLevelService.getUserTrainLevels(userId,currentPage);
    }
    @Log("I116-获取用户力量等级测试列表")
    @ApiOperation(value = "I116-获取用户力量等级测试列表",notes = "I116-获取用户力量等级测试列表")
    @GetMapping("/getUserPowerLevels")
    public Result getUserPowerLevels(@RequestParam @NonNull String userId){

        return iUserPowerLevelTestService.getUserPowerLevels(userId);
    }

    @Log("I11X-获取用户力量测试成绩")
    @ApiOperation(value = "I11X-获取用户力量测试成绩",notes = "I11X-获取用户力量测试成绩")
    @GetMapping("/getActionPowerTestResult")
    public Result getActionPowerTestResult(@RequestParam @NonNull String userId){

        return iUserActionPowerTestService.getActionPowerTestResult(userId);
    }

    @Log("I11X-获取用户力量测试项")
    @ApiOperation(value = "I11X-获取用户力量测试项",notes = "I11X-获取用户力量测试项")
    @GetMapping("/getActionPowerTestList")
    public Result getActionPowerTestList(@RequestParam @NonNull String userId){

        return iUserActionPowerTestService.getActionPowerTestList(userId);
    }

    @Log("I11X-保存用户力量测试")
    @ApiOperation(value = "I11X-保存用户力量测试",notes = "I11X-保存用户力量测试")
    @PostMapping("/saveActionPowerTest")
    public Result saveActionPowerTest(@RequestBody @Validated UserActionPowerTestDto userActionPowerTestDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iUserActionPowerTestService.saveUserTranPower(userActionPowerTestDto.getUserActionPowerTestRequests());
    }

    @Log("保存用户健康数据")
    @ApiOperation(value = "I111-保存用户健康数据",notes = "I111-保存用户健康数据")
    @PostMapping("/saveUserHealth")
    public Result saveUserHealth(@RequestBody @Validated UserHealthRequest request, BindingResult bindingResult){
        //userId从token获取
        String userId = UserUtils.getUserId();
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return userHealthService.addOrUpdate(userId, request);
    }

    @Log("获取用户健康数据")
    @ApiOperation(value = "获取用户健康数据",notes = "获取用户健康数据")
    @GetMapping("/getUserHealth")
    public Result<UserHealthDto> getUserHealth(@RequestParam(required = false) String userId){
        if (StringUtils.isEmpty(userId)){
            //userId从token获取
            userId = UserUtils.getUserId();
        }
        return userHealthService.getHealth(userId);
    }

    @Log("获取用户每日健康数据")
    @ApiOperation(value = "获取用户每日健康数据",notes = "获取用户每日健康数据")
    @GetMapping("/getUserHealthByDateList")
    public Result<UserHealthListDto> getUserHealthByDateList(@RequestParam(required = false) String userId,
                                                             @RequestParam(required = true) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate beginDate,
                                                             @RequestParam(required = true) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endDate){
        if (StringUtils.isEmpty(userId)){
            //userId从token获取
            userId = UserUtils.getUserId();
        }
        return userHealthService.getHealthByDate(userId,beginDate,endDate);
    }

    @Log("获取用户已训练课程列表")
    @ApiOperation(value = "获取用户已训练课程列表",notes = "获取用户已训练课程列表")
    @GetMapping("/getUserTrainCourseList")
    public Result getUserTrainCourseList(@RequestParam String userId,@RequestParam String currentPage){
        return iUserInfoService.getUserTrainCourseList(userId,currentPage);
    }







}