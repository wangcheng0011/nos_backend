package com.knd.manage.equip.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.equip.service.IEquipmentReportInfoService;
import com.knd.manage.equip.vo.VoSaveReportInfo;
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
public class EquipmentReportInfoController {

    @Resource
    private IEquipmentReportInfoService iEquipmentReportInfoService;

    @Log("I265-新增设备上传信息")
    @ApiOperation(value = "I265-新增设备上传信息")
    @PostMapping("/saveReportInfo")
    public Result saveReportInfo(@RequestBody @Validated VoSaveReportInfo vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iEquipmentReportInfoService.saveReportInfo(vo.getUserId(), vo.getEquipmentNo(), vo.getTurnOnTime(),
                vo.getHardVersion(), vo.getMainboardVersion(), vo.getPositionInfo(), vo.getAppVersion());
    }

    @Log("I264-获取设备上传信息列表")
    @ApiOperation(value = "I264-获取设备上传信息列表")
    @GetMapping("/getReportInfoList")
    public Result getReportInfoList(String equipmentNo, String current) {
        return iEquipmentReportInfoService.getReportInfoList(equipmentNo, current);
    }


}

