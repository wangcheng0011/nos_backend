package com.knd.manage.basedata.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.basedata.service.PowerTestService;
import com.knd.manage.basedata.vo.VoSavePowerTest;
import com.knd.manage.common.vo.VoId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zm
 */
@Api(tags = "云端管理-basedata")
@RestController
@CrossOrigin
@RequestMapping("/admin/basedata/powertest")
@RequiredArgsConstructor
public class BasePowerTestController {
    private final PowerTestService powerTestService;

    @Log("获取力量测试项列表")
    @ApiOperation(value = "获取力量测试项列表")
    @GetMapping("/getPowerTestList")
    public Result getPowerTestList(@ApiParam(value = "性别 0男1女") @RequestParam(required = false) String gender,
                                   @ApiParam(value = "难度id") @RequestParam(required = false) String difficultyId,
                                   @ApiParam(value = "当前页") @RequestParam(required = true) String current) {
        return powerTestService.getPowerTestList(gender,difficultyId,current);
    }

    @Log("获取力量测试项详情")
    @ApiOperation(value = "获取力量测试项详情")
    @GetMapping("/getPowerTestById")
    public Result getPowerTestById(@ApiParam(value = "力量测试id") @RequestParam(required = true) String id) {
        //校验参数
        if (StringUtils.isEmpty(id)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return powerTestService.getPowerTest(id);
    }


    @Log("保存力量测试项")
    @ApiOperation(value = "保存力量测试项")
    @PostMapping("/savePowerTest")
    public Result savePowerTest(@RequestBody @Validated VoSavePowerTest vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }

        //判断结果
        if (bindingResult.hasErrors()) {
            //参数校验失败
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        if (vo.getPostType().equals("1")) {
            //新增
            return powerTestService.addPowerTest(vo);
        } else {
            //更新
            return powerTestService.editPowerTest(vo);
        }
    }


    @Log("删除力量测试项")
    @ApiOperation(value = "删除力量测试项")
    @PostMapping("/deletePowerTest")
    public Result deletePowerTest(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return powerTestService.deletePowerTest(vo);
    }





}

