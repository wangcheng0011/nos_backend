package com.knd.front.train.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.front.train.service.IBaseTargetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liulongxiang
 * @className
 * @description
 * @date 2020/7/20
 * @Version 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("/front/train")
@Slf4j
@Api(tags = "train")
public class BaseTargetController {
    @Autowired
    private IBaseTargetService iBaseTargetService;
    @Deprecated
    @ApiOperation(value = "I220-获取目标列表",notes = "I220-获取目标列表")
    @Log("获取目标列表")
    @GetMapping("/getTargetList")
    public Result getTargetList(){
        return iBaseTargetService.getTargetList();
    }
}