package com.knd.manage.food.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.food.service.IFoodRecordService;
import com.knd.manage.food.vo.VoQueryFoodRecord;
import com.knd.manage.food.vo.VoSaveFoodRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "云端管理-food")
@RestController
@CrossOrigin
@RequestMapping("/admin/food")
@RequiredArgsConstructor
public class FoodRecordController {

    @Resource
    private IFoodRecordService iFoodRecordService;

    @Log("I252-维护食物记录")
    @ApiOperation(value = "I252-维护食物")
    @PostMapping("/saveFoodRecord")
    public Result saveFood(@RequestBody @Validated VoSaveFoodRecord vo , BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        //判断操作类型
        if (vo.getPostType().equals("1")) {
            //新增
            return iFoodRecordService.add(vo);
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return iFoodRecordService.edit( vo);
        }
    }



    @Log("I251-删除食物记录")
    @ApiOperation(value = "I251-删除食物记录")
    @PostMapping("/deleteFoodRecord")
    public Result deleteFoodRecord(@RequestBody  @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iFoodRecordService.deleteFoodRecord(vo.getUserId(), vo.getId());
    }

    @Log("I253-获取食物记录")
    @ApiOperation(value = "I253-获取食物记录")
    @GetMapping("/getFoodRecord")
    public Result getFoodRecord(String id) {
        //数据检查
        if (StringUtils.isEmpty(id)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iFoodRecordService.getFoodRecord(id);
    }
    @Log("I250-获取食物记录列表")
    @ApiOperation(value = "I250-获取食物记录列表")
    @GetMapping("/getFoodRecordList")
    public Result getFoodRecordList(VoQueryFoodRecord voQueryFoodRecord, String current) {
        return iFoodRecordService.getFoodRecordList(voQueryFoodRecord, current);
    }






}

