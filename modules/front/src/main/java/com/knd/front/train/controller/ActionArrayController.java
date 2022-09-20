package com.knd.front.train.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.train.request.ActionArrayTrainInfoRequest;
import com.knd.front.train.request.GetActionArrayRequest;
import com.knd.front.train.request.SaveActionArrayRequest;
import com.knd.front.train.request.UpdateActionArrayRequest;
import com.knd.front.train.service.IActionArrayService;
import com.knd.front.train.service.ITrainActionArrayHeadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author will
 */
@RestController
@CrossOrigin
@RequestMapping("/front/train")
@Slf4j
@Api(tags = "train")
public class ActionArrayController {
    @Autowired
    private ITrainActionArrayHeadService iTrainActionArrayHeadService;
    @Autowired
    private IActionArrayService iActionArrayService;


    @Log("I11X-获取用户动作组合列表")
    @ApiOperation(value = "I11X-获取用户动作组合列表", notes = "I11X-获取用户动作组合列表")
    @GetMapping("/getUserTrainInfo")
    public Result getUserActionArray(@Valid GetActionArrayRequest getActionArrayRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iActionArrayService.getUserActionArray(getActionArrayRequest);
    }

    @Log("I11X-获取用户动作组合详情")
    @ApiOperation(value = "I11X-获取用户动作组合详情", notes = "I11X-获取用户动作组合详情")
    @GetMapping("/getUserActionArrayInfo")
    public Result getUserActionArrayInfo(@RequestParam @NonNull String userId, @RequestParam String actionArrayId) {
        return iActionArrayService.getUserActionArrayInfo(userId,actionArrayId);
    }
    @Log("I11X-删除用户动作组合")
    @ApiOperation(value = "I11X-删除用户动作组合", notes = "I11X-删除用户动作组合")
    @GetMapping("/deleteUserActionArray")
    public Result deleteUserActionArray(@RequestParam @NonNull String userId, @RequestParam  @NonNull String actionArrayId) {
        return iActionArrayService.deleteUserActionArray(userId,actionArrayId);
    }

    @Log("I11X-保存动作组合")
    @ApiOperation(value = "I11X-保存动作组合",notes = "I11X-保存动作组合")
    @PostMapping("/saveActionArray")
    public Result saveActionArray(@RequestBody @Valid SaveActionArrayRequest saveActionArrayRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iActionArrayService.saveActionArray(saveActionArrayRequest);
    }

    @Log("I11X-更新动作组合")
    @ApiOperation(value = "I11X-更新动作组合",notes = "I11X-更新动作组合")
    @PostMapping("/updateActionArray")
    public Result updateActionArray(@RequestBody @Valid UpdateActionArrayRequest updateActionArrayRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iActionArrayService.updateActionArray(updateActionArrayRequest);
    }

    @ApiOperation(value = "I11X-提交组合训练结果",notes = "I11X-提交组合训练结果")
    @Log("I11X-提交组合训练结果")
    @PostMapping(value = "/commitActionArrayTrainInfo")
    public Result commitActionArrayTrainInfo(@RequestBody @Valid ActionArrayTrainInfoRequest actionArrayTrainInfoRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iTrainActionArrayHeadService.commitActionArrayTrainInfo(actionArrayTrainInfoRequest);
    }

    @ApiOperation(value = "I11X-获取组合训练结果列表",notes = "I11X-获取训练结果列表")
    @Log("I11X-获取训练结果列表")
    @GetMapping(value = "/getActionArrayTrainList")
    public Result getActionArrayTrainList(@RequestParam("userId") @NotNull String userId,@RequestParam("currentPage") @NotNull String currentPage){

        return iTrainActionArrayHeadService.getActionArrayTrainList(userId,currentPage);
    }

    @ApiOperation(value = "I11X-获取组合训练结果详情",notes = "I11X-获取训练结果详情")
    @Log("I11X-获取训练结果详情")
    @GetMapping(value = "/getActionArrayTrainDetail")
    public Result getActionArrayTrainDetail(@RequestParam("id") @NotNull String id){

        return iTrainActionArrayHeadService.getActionArrayTrainDetail(id);
    }
}