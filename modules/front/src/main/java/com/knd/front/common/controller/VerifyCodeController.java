package com.knd.front.common.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.common.dto.CodeDto;
import com.knd.front.common.service.IVerifyCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/6/30
 * @Version 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("/front/common")
@Api(tags = "common")
@Slf4j
public class VerifyCodeController {
    @Autowired
    private IVerifyCodeService iVerifyCodeService;

    @Log("I030-获取验证码")
    @ApiOperation(value = "I030-获取验证码 " ,notes = "I030-获取验证码 ")
    @PostMapping("/getVerificationCode")
    public Result getVerificationCode(@RequestBody @Valid CodeDto codeDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iVerifyCodeService.getVerificationCode(codeDto);
    }
}