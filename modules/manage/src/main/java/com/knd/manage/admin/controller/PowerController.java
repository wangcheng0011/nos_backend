package com.knd.manage.admin.controller;


import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.manage.admin.service.IPowerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Api(tags = "云端管理-admin")
@RestController
@CrossOrigin
@RequestMapping("/admin/admin")
public class PowerController {
     @Resource
    private IPowerService iPowerService;

    @Log("I353-查询权限信息")
    @ApiOperation(value = "I353-查询权限信息")
    @GetMapping("/getPowerList")
    public Result getPowerList(String id) {
        return iPowerService.getPowerList(id);
    }



}

