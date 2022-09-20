package com.knd.front.home.controller;


import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.front.home.service.IBaseCourseTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author llx
 * @since 2020-07-01
 */
@RestController
@CrossOrigin
@RequestMapping("/front/home")
@Api(tags = "home")
@Slf4j
public class BaseCourseTypeController {
    @Autowired
    private IBaseCourseTypeService iBaseCourseTypeService;

    @GetMapping("/getHomeCourseList")
    @Log("I050-获取分类课程列表")
    @ApiOperation(value = "I050-获取分类课程列表", notes = "I050-获取分类课程列表")
    public Result getHomeCourseList(@RequestParam(required = false) String userId) {
        return iBaseCourseTypeService.getHomeCourseList(userId);
    }




}

