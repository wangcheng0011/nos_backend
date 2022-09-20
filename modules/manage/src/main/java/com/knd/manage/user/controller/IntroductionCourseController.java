package com.knd.manage.user.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.user.service.IIntroductionCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;

@Api(tags = "云端管理-user")
@RestController
@CrossOrigin
@RequestMapping("/admin/user")
public class IntroductionCourseController {

    @Resource
    private IIntroductionCourseService iIntroductionCourseService;

    @Log("I314-查询注册会员课程列表")
    @ApiOperation(value = "I314-查询注册会员课程列表")
    @GetMapping("/queryUserCourseList")
    public Result queryUserCourseList(String nickName, String mobile, String equipmentNo, String trainTimeBegin,
                                        String trainTimeEnd, String current) throws ParseException {
        //数据检查
        if (StringUtils.isEmpty(current)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iIntroductionCourseService.queryUserCourseList(nickName, mobile, equipmentNo, trainTimeBegin, trainTimeEnd, current);
    }



    @Log("I314-查询注册会员课程列表")
    @ApiOperation(value = "I314-查询注册会员课程列表")
    @GetMapping("/queryCourseList")
    public Result queryCourseList(String current) throws Exception {

        return iIntroductionCourseService.queryCourseList(current);
    }



}

