package com.knd.manage.food.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.food.service.IFoodService;
import com.knd.manage.food.vo.VoSaveFood;
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
public class FoodController {

    @Resource
    private IFoodService iFoodService;

    @Log("I252-维护食物")
    @ApiOperation(value = "I252-维护食物")
    @PostMapping("/saveFood")
    public Result saveFood(@RequestBody @Validated VoSaveFood vo , BindingResult bindingResult) {
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
            return iFoodService.add(vo);
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return iFoodService.edit(vo);
        }
    }



    @Log("I251-删除食物")
    @ApiOperation(value = "I251-删除食物")
    @PostMapping("/deleteFood")
    public Result deleteFood(@RequestBody  @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iFoodService.deleteFood(vo.getUserId(), vo.getId());
    }

    @Log("I253-获取食物")
    @ApiOperation(value = "I253-获取食物")
    @GetMapping("/getFood")
    public Result getFood(String id) {
        //数据检查
        if (StringUtils.isEmpty(id)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iFoodService.getFood(id);
    }
    @Log("I250-获取食物列表")
    @ApiOperation(value = "I250-获取食物列表")
    @GetMapping("/getFoodList")
    public Result getFoodList(String name, String current) {
        return iFoodService.getFoodList(name, current);
    }






}

