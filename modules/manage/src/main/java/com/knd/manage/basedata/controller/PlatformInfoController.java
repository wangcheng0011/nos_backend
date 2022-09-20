package com.knd.manage.basedata.controller;


import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.manage.basedata.service.IPlatformInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "云端管理-basedata")
@RestController
@CrossOrigin
@RequestMapping("/admin/basedata")
public class PlatformInfoController {
    @Resource
    private IPlatformInfoService iPlatformInfoService;

    @Log("I200-获取后管首页统计信息")
    @ApiOperation(value = "I200-获取后管首页统计信息")
    @GetMapping("/getPlatformInfo")
    public Result getPlatformInfo() {
        return iPlatformInfoService.getPlatformInfo();
    }



}
