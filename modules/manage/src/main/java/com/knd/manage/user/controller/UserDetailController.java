package com.knd.manage.user.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.user.service.IUserDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sy
 * @since 2020-07-07
 */
@Api(tags = "云端管理-user")
@RestController
@CrossOrigin
@RequestMapping("/admin/user")
public class UserDetailController {
    @Resource
    private IUserDetailService iUserDetailService;


    @Log("I316-查询注册会员详情")
    @ApiOperation(value = "I316-查询注册会员详情")
    @GetMapping("/getUserDetail")
    public Result getUserDetail(String userId) {
        //数据检查
        if (StringUtils.isEmpty(userId)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iUserDetailService.getUserDetail(userId);
    }

}

