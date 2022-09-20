package com.knd.manage.homePage.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.homePage.dto.DeviceAnalysisDto;
import com.knd.manage.homePage.service.BuyAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author zm
 */
@Api(tags = "首页大屏数据")
@RestController
@CrossOrigin
@RequestMapping("/admin/homePage")
@RequiredArgsConstructor
@Slf4j
public class BuyAnalysisController {
    private final BuyAnalysisService buyAnalysisService;

    @Log("获取各难度,分类课程数量")
    @ApiOperation(value = "获取各难度,分类课程数量")
    @GetMapping("/getDiffCourseNum")
    public Result getDiffCourseNum(@ApiParam("查询类型：0难度 1分类") @RequestParam(required = true) String type){
        if ("0".equals(type)){
            return buyAnalysisService.queryDiffCourseNum();
        }else if ("1".equals(type)){
            return buyAnalysisService.queryTypeCourseNum();
        }else {
            return ResultUtil.error(ResultEnum.FAIL.getCode(),"查询类型错误");
        }
    }

    @Log("查询课程,系列课程，自由训练，训练计划数量与日期增长")
    @ApiOperation(value = "查询课程,系列课程，自由训练，训练计划数量与日期增长0826")
    @GetMapping("/getCourseByTypeNum")
    public Result getCourseByTypeNum(@ApiParam("开始日期（yyyy-MM-dd）") @RequestParam(required = true) String beginDate,
                                     @ApiParam("截止日期（yyyy-MM-dd）") @RequestParam(required = true) String endDate){
        return buyAnalysisService.queryCourseByTypeNum(beginDate,endDate);
    }

    @Log("查询减脂/塑性/增肌/系列课程数量与日期增长")
    @ApiOperation(value = "查询减脂/塑性/增肌/系列课程数量与日期增长0826")
    @GetMapping("/getCourseByTargetNum")
    public Result getCourseByTargetNum(@ApiParam("开始日期（yyyy-MM-dd）") @RequestParam(required = true) String beginDate,
                                     @ApiParam("截止日期（yyyy-MM-dd）") @RequestParam(required = true) String endDate){
        return buyAnalysisService.queryCourseByTargetNum(beginDate,endDate);
    }

    @Log("获取各分类用户数量")
    @ApiOperation(value = "获取各分类用户数量")
    @GetMapping("/getDiffUserNum")
    public Result getDiffUserNum(){
        return buyAnalysisService.queryDiffUserNum();
    }

    @Log("获取各分类用户,课程,设备,计划数量")
    @ApiOperation(value = "获取各分类用户数量")
    @GetMapping("/getDiffUserDeviceCoursePlanNum")
    public Result getDiffUserDeviceCoursePlanNum(){
        return buyAnalysisService.queryDiffUserDeviceCoursePlanNum();
    }

    @Log("获取vip会员比例")
    @ApiOperation(value = "获取vip会员比例")
    @GetMapping("/getPercentOfVip")
    public Result getPercentOfVip(){
        return ResultUtil.success(buyAnalysisService.getPercentOfVip());
    }

    @Log("获取性别比例")
    @ApiOperation(value = "获取性别比例")
    @GetMapping("/getGenderPercent")
    public Result getGenderPercent(){
        return ResultUtil.success(buyAnalysisService.getGenderPercent());
    }

    @Log("获取年龄比例")
    @ApiOperation(value = "获取年龄比例")
    @GetMapping("/getAgePercent")
    public Result getAgePercent(){
        return ResultUtil.success(buyAnalysisService.queryAgePercent());
    }

    @Log("查询周会员增长")
    @ApiOperation(value = "查询周会员增长")
    @GetMapping("/getUserGrowthOfWeeks")
    public Result getUserGrowthOfWeeks(@ApiParam("开始日期（yyyy-MM-dd）") @RequestParam(required = true) String beginDate,
        @ApiParam("截止日期（yyyy-MM-dd）") @RequestParam(required = true) String endDate){
        return ResultUtil.success(buyAnalysisService.queryUserGrowthOfWeeks(beginDate,endDate));
    }

    @Log("查询日会员增长0825")
    @ApiOperation(value = "查询日会员增长0825")
    @GetMapping("/getUserGrowthOfDay")
    public Result getUserGrowthOfDay(@ApiParam("开始日期（yyyy-MM-dd）") @RequestParam(required = true) String beginDate,
                                       @ApiParam("截止日期（yyyy-MM-dd）") @RequestParam(required = true) String endDate){
        return ResultUtil.success(buyAnalysisService.queryUserGrowthOfDay(beginDate,endDate));
    }

    @Log("配件购买排行0826")
    @ApiOperation(value = "配件购买排行0826")
    @GetMapping("/getPartsPurchase")
    public Result getPartsPurchase(){
        return ResultUtil.success(buyAnalysisService.getPartsPurchase());
    }

    @Log("全部设备的位置信息0907")
    @ApiOperation(value = "全部设备的位置信息0907")
    @GetMapping("/getEquipmentAddress")
    public Result getEquipmentAddress(@ApiParam("查询类型：0省 1市 2区") @RequestParam(required = true) String type){
        return buyAnalysisService.queryEquipmentAddress(type);
    }

    @Log("用户消费排行榜0831")
    @ApiOperation(value = "用户消费排行榜0831")
    @GetMapping("/getUserPayRanking")
    public Result getUserPayRanking() {
        return ResultUtil.success(buyAnalysisService.queryUserPayRanking());
    }

    @Log("故障分析0827")
    @ApiOperation(value = "故障分析0827")
    @GetMapping("/getFaultAnalysis")
    public Result getFaultAnalysis(){
        return ResultUtil.success(buyAnalysisService.getFaultAnalysis());
    }

    @Log("设备分析0831")
    @ApiOperation(value = "设备分析0831")
    @GetMapping("/getDeviceAnalysis")
    public Result<DeviceAnalysisDto> getDeviceAnalysis(){
        return buyAnalysisService.getDeviceAnalysis();
    }

    @Log("浏览商品0901")
    @ApiOperation(value = "浏览商品0901")
    @GetMapping("/getViewRecords")
    public Result getViewRecords(@ApiParam("开始日期（yyyy-MM-dd）") @RequestParam(required = true) String beginDate,
                                 @ApiParam("截止日期（yyyy-MM-dd）") @RequestParam(required = true) String endDate){
        return ResultUtil.success(buyAnalysisService.getViewRecords(beginDate,endDate));
    }

    @Log("配使用分析0902")
    @ApiOperation(value = "配使用分析0902")
    @GetMapping("/getPartsUsageAnalysis")
    public Result getPartsUsageAnalysis(){
        return ResultUtil.success(buyAnalysisService.getPartsUsageAnalysis());
    }

    @Log("训练难度列表0902")
    @ApiOperation(value = "训练难度列表0902")
    @GetMapping("/getTrainDifficultNum")
    public Result<DeviceAnalysisDto> getTrainDifficultNum(){
        return ResultUtil.success(buyAnalysisService.getTrainDifficultNum());
    }

    @Log("器材使用频率0906")
    @ApiOperation(value = "器材使用频率0906")
    @GetMapping("/getEquipmentUseFrequency")
    public Result getEquipmentUseFrequency(@ApiParam("开始日期（yyyy-MM-dd）") @RequestParam(required = true) String beginDate,
                                           @ApiParam("截止日期（yyyy-MM-dd）") @RequestParam(required = true) String endDate){
        return buyAnalysisService.queryEquipmentUseFrequency(beginDate, endDate);
    }

    @Log("获取设备状态数量0910")
    @ApiOperation(value = "获取设备状态数量0910")
    @GetMapping("/getEquipmentStatusNum")
    public Result getEquipmentStatusNum() {
        return ResultUtil.success(buyAnalysisService.getEquipmentStatusNum());
    }
}
