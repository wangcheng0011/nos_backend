package com.knd.manage.equip.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.equip.service.IAppReleaseVersionService;
import com.knd.manage.equip.vo.VoGetApkInfoList;
import com.knd.manage.equip.vo.VoSaveApkInfo;
import com.knd.manage.equip.vo.VoSaveReleaseStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "云端管理-equip")
@RestController
@CrossOrigin
@RequestMapping("/admin/equip")
public class AppReleaseVersionController {
    @Resource
    private IAppReleaseVersionService iAppReleaseVersionService;

    @Log("I331-维护apk版本信息")
    @ApiOperation(value = "I331-维护apk版本信息")
    @PostMapping("/saveApkInfo")
    public Result saveApkInfo(@RequestBody @Validated VoSaveApkInfo vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (vo.getPostType().equals("1")) {
            return iAppReleaseVersionService.add(vo);
        } else {
            return iAppReleaseVersionService.edit(vo);
        }
    }


    @Log("I332-获取apk版本信息")
    @ApiOperation(value = "I332-获取apk版本信息")
    @GetMapping("/getApkInfo")
    public Result getApkInfo(String id) {
        //数据检查
        if (StringUtils.isEmpty(id)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iAppReleaseVersionService.getApkInfo(id);
    }

    @Log("I333-删除apk版本信息")
    @ApiOperation(value = "I333-删除apk版本信息")
    @PostMapping("/deleteApkInfo")
    public Result deleteApkInfo(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iAppReleaseVersionService.deleteApkInfo(vo);
    }


    @Log("I334-获取apk版本信息列表")
    @ApiOperation(value = "I334-获取apk版本信息列表")
    @GetMapping("/getApkInfoList")
    public Result getApkInfoList(@Validated VoGetApkInfoList vo  , BindingResult bindingResult) {
        //数据检查
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iAppReleaseVersionService.getApkInfoList(vo);
    }

    @Log("I335-修改apk版本发布状态")
    @ApiOperation(value = "I335-修改apk版本发布状态")
    @PostMapping("/saveReleaseStatus")
    public Result saveReleaseStatus(@RequestBody @Validated VoSaveReleaseStatus vo,BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iAppReleaseVersionService.saveReleaseStatus(vo.getUserId(),vo.getId(),vo.getReleaseStatus());
    }

}

