package com.knd.front.train.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.train.request.FreeTrainInfoRequest;
import com.knd.front.train.request.FreeTrainingInfoRequest;
import com.knd.front.train.request.TrainCourseInfoRequest;
import com.knd.front.train.service.IBaseBodyPartService;
import com.knd.front.train.service.ICourseDetailService;
import com.knd.front.train.service.ITrainFreeHeadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/7
 * @Version 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("/front/train")
@Slf4j
@Api(tags = "train")
public class FreeTrainController {
    @Autowired
    private ICourseDetailService iCourseDetailService;
    @Autowired
    private IBaseBodyPartService iBaseBodyPartService;
    @Autowired
    private ITrainFreeHeadService iTrainFreeHeadService;
    @Log("I082-获取动作详情页")
    @ApiOperation(value = "I082-获取动作详情页",notes = "I082-获取动作详情页")
    @GetMapping("/getFreeTrainDetail")
    public Result getFreeTrainDetail(@RequestParam @NotBlank String actionId, @RequestParam(required = false) String userId){
        return iCourseDetailService.getFreeTrainDetail(actionId,userId);
    }
    @PostMapping("/commitTrainCourseInfo")
    @Log("I070-提交课程训练结果")
    @ApiOperation(value = "I070-提交课程训练结果",notes = "I070-提交课程训练结果")
    public Result commitTrainCourseInfo(@RequestBody @Valid TrainCourseInfoRequest trainCourseInfoRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iBaseBodyPartService.commitTrainCourseInfo(trainCourseInfoRequest);
    }
    @GetMapping(value = "/getFilterFreeTrainLabelSettings")
    @Log("I080-获取动作筛序分类标签")
    @ApiOperation(value = "I080-获取动作筛序分类标签",notes = "I080-获取动作筛序分类标签")
    public Result getFilterFreeTrainLabelSettings(@RequestParam(value = "userId",required = false) String userId){
        return iBaseBodyPartService.getFilterFreeTrainLabelSettings(userId);
    }
    @ApiOperation(value = "I083-提交动作结果",notes = "I083-提交动作结果")
    @Log("I083-提交动作结果")
    @PostMapping(value = "/commitFreeTrainInfo")
    public Result commitFreeTrainInfo(@RequestBody @Valid FreeTrainInfoRequest freeTrainInfoRequest,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iTrainFreeHeadService.commitFreeTrainInfo(freeTrainInfoRequest);
    }

    @ApiOperation(value = "I083-提交自由训练结果",notes = "I083-提交自由训练结果")
    @Log("I083-提交自由训练结果")
    @PostMapping(value = "/commitFreeTrainingInfo")
    public Result commitFreeTrainingInfo(@RequestBody @Valid FreeTrainingInfoRequest freeTrainingInfoRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iTrainFreeHeadService.commitFreeTrainingInfo(freeTrainingInfoRequest);
    }
}