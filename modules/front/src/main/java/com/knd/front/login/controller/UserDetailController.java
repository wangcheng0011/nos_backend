package com.knd.front.login.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.front.entity.User;
import com.knd.front.login.request.UserDetailRequest;
import com.knd.front.login.service.IUserDetailService;
import com.knd.front.login.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author llx
 * @since 2020-07-01
 */
@RestController
@CrossOrigin
@RequestMapping("/front/login")
@Api(tags = "login")
public class UserDetailController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private IUserDetailService iUserDetailService;



    @Log("I043-完善用户资料")
    @ApiOperation(value = "I043-完善用户资料",notes = "I043-完善用户资料")
    @PostMapping("/saveUserDetail")
    public Result saveUserDetail(@RequestBody @Valid UserDetailRequest userDetailRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", userDetailRequest.getUserId());
        queryWrapper.eq("deleted",0);
        queryWrapper.eq("frozenFlag",0);
        User isUserExist = iUserService.getOne(queryWrapper);
        if (StringUtils.isEmpty(isUserExist)){
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iUserDetailService.updateUserDetail(userDetailRequest);
    }

    @Log("I044-查询用户资料详情")
    @ApiOperation(value = "I044-查询用户资料详情",notes = "I044-查询用户资料详情")
    @GetMapping("/getUserDetail")
    public Result getUserDetail(@RequestParam @NotBlank String userId){
        return iUserDetailService.getUserDetail(userId);

    }
}

