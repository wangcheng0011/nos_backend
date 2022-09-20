package com.knd.manage.basedata.controller;


import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.manage.basedata.service.IBaseActionTypeService;
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
public class BaseActionTypeController {

    @Resource
    private IBaseActionTypeService iBaseActionTypeService;


    @Log("I21X-获取动作类型列表")
    @ApiOperation(value = "I21X-获取动作类型列表")
    @GetMapping("/getActionTypeList")
    public Result getActionTypeList() {
        return iBaseActionTypeService.getActionTypeList();
    }


}

