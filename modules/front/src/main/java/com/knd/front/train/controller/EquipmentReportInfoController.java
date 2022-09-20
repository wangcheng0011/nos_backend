package com.knd.front.train.controller;


import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.train.request.EquipmentReportInfoRequest;
import com.knd.front.train.service.IEquipmentReportInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author llx
 * @since 2020-07-08
 */
@RestController
@CrossOrigin
@RequestMapping("/front/train")
@Slf4j
@Api(tags = "train")
public class EquipmentReportInfoController {
    @Autowired
    private IEquipmentReportInfoService iEquipmentReportInfoService;

    @PostMapping("/commitEquipmentReportInfo")
    //@Log("I100-设备信息上报")
    @ApiOperation(value = "I100-设备信息上报",notes = "I100-设备信息上报")
    public Result commitEquipmentReportInfo(@RequestBody @Valid EquipmentReportInfoRequest equipmentReportInfoRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iEquipmentReportInfoService.commitEquipmentReportInfo(equipmentReportInfoRequest);
    }
}

