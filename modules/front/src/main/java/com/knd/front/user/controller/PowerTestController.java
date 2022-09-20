package com.knd.front.user.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.user.dto.PowerTestDto;
import com.knd.front.user.dto.UserActionPowerTestDto;
import com.knd.front.user.request.PowerTestResultRequest;
import com.knd.front.user.service.IPowerTestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 力量测试
 * @author will
 * @date 2021/8/5 13:56
 */
@RestController
@CrossOrigin
@RequestMapping("/front/user/powerTest")
@Slf4j
@Api(tags = "user")
@RequiredArgsConstructor
public class PowerTestController {

    private final IPowerTestService powerTestService;

    @ApiOperation(value = "获取力量测试项目", notes = "获取力量测试项目")
    @Log("获取力量测试项目")
    @GetMapping("/getPwoerTest")
    public Result getPwoerTest() {

        List<PowerTestDto> powerTest = powerTestService.getPowerTest();
        return ResultUtil.success(powerTest);
    }

    @ApiOperation(value = "获取用户力量测试结果", notes = "获取用户力量测试结果")
    @Log("获取用户力量测试结果")
    @PostMapping("/getUserPowerTestResult")
    public Result getUserPowerTestResult(){

        return ResultUtil.success(null);

    }

    @Log("保存用户力量测试结果")
    @ApiOperation(value = "保存用户力量测试结果",notes = "保存用户力量测试结果")
    @PostMapping("/saveUserPowerTestResult")
    public Result saveUserPowerTestResult(@RequestBody @Validated PowerTestResultRequest request, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return powerTestService.addTestResult(request);
    }

}