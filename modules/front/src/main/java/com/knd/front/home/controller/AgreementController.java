package com.knd.front.home.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.front.home.service.IAgreementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author will
 */
@RestController
@CrossOrigin
@RequestMapping("/front/home")
@Slf4j
@Api(tags = "home")
public class AgreementController {
    private IAgreementService iAgreementService;
    @Autowired
    public AgreementController(IAgreementService iAgreementService) {
        this.iAgreementService = iAgreementService;
    }
    @Log("I001X-获取协议信息")
    @ApiOperation(value = "I001X-获取协议信息",notes = "I001X-获取协议信息")
    @GetMapping("/getAgreementByName")
    public Result getAgreementByName(@RequestParam(required = false) String agreementName) {
//        log.info("controller传进来的是什么："+currentAppVersion);

        return iAgreementService.getAgreement(agreementName);
    }
}