package com.knd.manage.basedata.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.basedata.service.IBaseActionService;
import com.knd.manage.basedata.vo.VoSaveAction;
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
public class BaseActionController {

    @Resource
    private IBaseActionService iBaseActionService;


    @Log("I242-维护动作信息")
    @ApiOperation(value = "I242-维护动作信息")
    @PostMapping("/saveAction")
    public Result saveAction(@RequestBody @Validated VoSaveAction vo, BindingResult bindingResult) {
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
        if (vo.getFreeTrainFlag().equals("1")){
            //自由训练动作必须上传图片和视频文件
            if (StringUtils.isEmpty(vo.getVideoAttachNewName(),vo.getPicAttachNewName())){
                //参数校验失败
                return ResultUtil.error("U0995", "自由训练动作必须上传图片和视频文件");
            }
        }
        if ( vo.getAimDuration() == null && vo.getAimTimes() == null) {
            //参数校验失败
            return ResultUtil.error("U0995", "默认计数和计时参数不可同时为空");
        }
        if (vo.getPostType().equals("1")) {
            //新增
            return iBaseActionService.add(vo);
        } else {
            //更新
            if (StringUtils.isEmpty(vo.getActionId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return iBaseActionService.edit(vo);
        }
    }


    @Log("I241-删除动作")
    @ApiOperation(value = "I241-删除动作")
    @PostMapping("/deleteAction")
    public Result deleteAction(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iBaseActionService.deleteAction(vo);
    }

    @Log("I243-获取动作")
    @ApiOperation(value = "I243-获取动作")
    @GetMapping("/getAction")
    public Result getAction(String actionId) {
        //校验参数
        if (StringUtils.isEmpty(actionId)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iBaseActionService.getAction(actionId);
    }


    @Log("I240-获取动作列表")
    @ApiOperation(value = "I240-获取动作列表")
    @GetMapping("/getActionList")
    public Result getActionList(String actionType,String target, String part, String action, String current) {
        return iBaseActionService.getActionList(actionType,target, part, action, current);
    }
}

