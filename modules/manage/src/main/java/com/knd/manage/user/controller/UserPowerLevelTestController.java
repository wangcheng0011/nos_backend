package com.knd.manage.user.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.user.service.IUserPowerLevelTestService;
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
public class UserPowerLevelTestController {
    @Resource
    private IUserPowerLevelTestService iUserPowerLevelTestService;

    @Log("I315-查询注册会员力量等级测试列表")
    @ApiOperation(value = "I315-查询注册会员力量等级测试列表")
    @GetMapping("/queryPowerLevelTestList")
    public Result queryPowerLevelTestList(String nickName, String mobile, String action, String trainTimeBegin,
                                          String trainTimeEnd, String current) throws ParseException {
        //数据检查
        if (StringUtils.isEmpty(current)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iUserPowerLevelTestService.queryPowerLevelTestList(nickName, mobile, action, trainTimeBegin, trainTimeEnd, current);
    }

}

