package com.knd.front.user.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.front.user.request.UserPayAddRequest;
import com.knd.front.user.request.UserPayCheckRequest;
import com.knd.front.user.request.UserPayEditRequest;
import com.knd.front.user.service.IUserPayService;
import com.knd.front.user.service.IUserPurchasedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zm
 */
@RestController
@CrossOrigin
@RequestMapping("/front/userPay")
@Slf4j
@Api(tags = "用户支付userPay")
@RequiredArgsConstructor
public class UserPayController {

    private final IUserPayService userPayService;
    private final IUserPurchasedService userPurchasedService;

    @Log("I212-新增用户支付记录")
    @ApiOperation(value = "I212-新增用户支付记录")
    @PostMapping("/savePay")
    public Result savePay(@RequestBody @Validated UserPayAddRequest request, BindingResult bindingResult) {
        //参数校验
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return userPayService.add(request);
    }

    @Log("I212-更新用户支付记录")
    @ApiOperation(value = "I212-更新用户支付记录")
    @PostMapping("/updatePay")
    public Result updatePay(@RequestBody @Validated UserPayEditRequest request, BindingResult bindingResult) {
        //参数校验
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return userPayService.edit(request);
    }

    @Log("I212-批量校验用户支付记录")
    @ApiOperation(value = "I212-批量校验用户支付记录")
    @PostMapping("/checkUserPayList")
    public Result checkUserPayList(@RequestBody @Validated UserPayCheckRequest request, BindingResult bindingResult){
        //参数校验
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return userPayService.checkList(request);

    }

    @Log("I111-校验用户支付记录")
    @ApiOperation(value = "I111-校验用户支付记录",notes = "I111-校验用户支付记录")
    @GetMapping("/checkUserPay")
    public Result checkUserPay(@RequestParam @NonNull String userId,
                               @RequestParam @NonNull Integer type,
                               @RequestParam @NonNull String payId){
        return userPayService.check(userId,type,payId);
    }

    @Log("获取用户已购课程列表")
    @ApiOperation(value = "获取用户已购课程列表",notes = "获取用户已购课程列表")
    @GetMapping("/getUserPurchasedCourse")
    public Result getUserPurchasedCourse(@RequestParam(required = true) String userId, @RequestParam(required = true) String currentPage){
        return userPurchasedService.getPurchasedCourse(userId,currentPage);
    }

}
