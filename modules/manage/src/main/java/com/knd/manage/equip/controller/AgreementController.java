package com.knd.manage.equip.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.equip.service.IAgreementService;
import com.knd.manage.equip.vo.VoSaveAgreement;
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
public class AgreementController {

    @Resource
    private IAgreementService iAgreementService;

    @Log("I26X1-协议")
    @ApiOperation(value = "I26X1-协议")
    @PostMapping("/saveAgreement")
    public Result saveAgreement(@RequestBody @Validated VoSaveAgreement vo, BindingResult bindingResult) {
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
            return iAgreementService.add(vo.getUserId(), vo.getAgreementName(), vo.getAgreementContent());
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return iAgreementService.edit(vo.getUserId(), vo.getAgreementName(), vo.getAgreementContent(), vo.getId());
        }

    }


    @Log("I261-删除协议")
    @ApiOperation(value = "I261-删除协议")
    @PostMapping("/deleteAgreement")
    public Result deleteAgreement(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iAgreementService.delete(vo.getUserId(), vo.getId());
    }

    @Log("I263-获取协议")
    @ApiOperation(value = "I263-获取协议")
    @GetMapping("/getAgreement")
    public Result getAgreement(String agreementName) {
        //数据检查
        if (StringUtils.isEmpty(agreementName)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iAgreementService.getAgreement(agreementName);
    }


    @Log("I260-获取协议列表")
    @ApiOperation(value = "I260-获取协议列表")
    @GetMapping("/getAgreementList")
    public Result getAgreementList(String agreementName, String current) {
        return iAgreementService.getAgreementList(agreementName, current);
    }


}

