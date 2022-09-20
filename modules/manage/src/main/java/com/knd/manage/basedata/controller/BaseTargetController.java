package com.knd.manage.basedata.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.basedata.service.IBaseTargetService;
import com.knd.manage.basedata.vo.VoSaveTarget;
import com.knd.manage.common.vo.VoId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Api(tags = "云端管理-basedata")
@RestController
@CrossOrigin
@RequestMapping("/admin/basedata")
public class BaseTargetController {

    @Resource
    private IBaseTargetService iBaseTargetService;

    @Log("I222-维护目标")
    @ApiOperation(value = "I222-维护目标")
    @PostMapping("/saveTarget")
    public Result saveTarget(@RequestBody @Validated VoSaveTarget vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //数据检查
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        //判断操作类型
        if (vo.getPostType().equals("1")) {
            //新增
            return iBaseTargetService.add(vo.getUserId(), vo.getTarget(), vo.getRemark());
        } else{
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getTargetId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return iBaseTargetService.edit(vo.getUserId(), vo.getTarget(), vo.getRemark(), vo.getTargetId());
        }

    }

    @Log("I221-删除目标")
    @ApiOperation(value = "I221-删除目标")
    @PostMapping("/deleteTarget")
    public Result deleteTarget(@RequestBody  @Validated VoId vo, BindingResult bindingResult) {
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iBaseTargetService.deleteTarget(vo.getUserId(), vo.getId());
    }

    @Log("I223-获取目标")
    @ApiOperation(value = "I223-获取目标")
    @GetMapping("/getTarget")
    public Result getTarget(String id) {
        //数据检查
        if (StringUtils.isEmpty(id)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iBaseTargetService.getTarget(id);
    }

    @Log("I220-获取目标列表")
    @ApiOperation(value = "I220-获取目标列表")
    @GetMapping("/getTargetList")
    public Result getTargetList(String target, String current) {
        return iBaseTargetService.getTargetList(target, current);
    }


}

