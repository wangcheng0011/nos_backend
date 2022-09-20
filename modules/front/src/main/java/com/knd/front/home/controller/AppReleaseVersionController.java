package com.knd.front.home.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.front.home.service.IAppReleaseVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/6/30
 * @Version 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("/front/home")
@Slf4j
@Api(tags = "home")
public class AppReleaseVersionController {
    private IAppReleaseVersionService iAppReleaseVersionService;
    @Autowired
    public AppReleaseVersionController(IAppReleaseVersionService iAppReleaseVersionService) {
        this.iAppReleaseVersionService = iAppReleaseVersionService;
    }
    @Log("I001-获取APP版本信息")
    @ApiOperation(value = "I001-获取APP版本信息",notes = "I001-获取APP版本信息")
    @GetMapping("/getAppReleaseVersion")
    public Result getAppReleaseVersion(@RequestParam(required = false) String currentAppVersion,
                                       @RequestParam(required = false) String currentFirmwareVersion,
                                       @RequestParam(required = false) String currentSystemVersion) {
//        log.info("controller传进来的是什么："+currentAppVersion);
        if(currentAppVersion==null){
            currentAppVersion="";
        }
        if(currentFirmwareVersion==null){
            currentFirmwareVersion="";
        }

        if(currentSystemVersion==null){
            currentSystemVersion="";
        }
        return iAppReleaseVersionService.getAppReleaseVersion(currentAppVersion,currentFirmwareVersion,currentSystemVersion);
    }
}