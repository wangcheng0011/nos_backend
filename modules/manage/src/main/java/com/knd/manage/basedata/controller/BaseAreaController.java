package com.knd.manage.basedata.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.manage.basedata.service.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "云端管理-basedata")
@RestController
@CrossOrigin
@RequestMapping("/admin/basedata")
@RequiredArgsConstructor
public class BaseAreaController {

    private final AreaService areaService;

    @Log("获取区域列表")
    @ApiOperation(value = "获取区域列表")
    @GetMapping("/getAreaList")
    public Result getAreaList() {
        return areaService.getAreaList();
    }
}
