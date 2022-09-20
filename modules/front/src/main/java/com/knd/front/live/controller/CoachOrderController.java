package com.knd.front.live.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.front.live.request.CancelOrderCoachRequest;
import com.knd.front.live.request.OrderCoachRequest;
import com.knd.front.live.service.CoachOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zm
 */
@RestController
@CrossOrigin
@RequestMapping("/front/live")
@Slf4j
@Api(tags = "教练预约")
@RequiredArgsConstructor
public class CoachOrderController {
    private final CoachOrderService coachOrderService;

//    @Log("获取用户预约教练课程全部记录")
//    @ApiOperation(value = "获取用户预约教练课程全部记录",notes = "获取用户预约教练课程全部记录")
//    @GetMapping("/getCoachIsOrderList")
//    public Result<CoachListDto> getCoachIsOrderList(@ApiParam("用户id") @RequestParam(required = true) String userId,
//                                             @ApiParam("当前页") @RequestParam(required = true) String current){
//        return coachOrderService.getCoachOrderList(userId,current);
//    }

    @Log("预约教练课程")
    @ApiOperation(value = "预约教练课程")
    @PostMapping("/orderTime")
    public Result orderTime(HttpServletResponse response, @RequestBody @Validated OrderCoachRequest request, HttpServletRequest httpServletRequest, BindingResult bindingResult) {
        if (StringUtils.isEmpty(request.getUserId())){
            //userId从token获取
            String userId = UserUtils.getUserId();
            request.setUserId(userId);
        }
        String platform = httpServletRequest.getHeader("platform");
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return coachOrderService.orderTime(response,request,platform);
    }

    @Log("预约教练通过")
    @ApiOperation(value = "预约教练通过")
    @PostMapping("/orderSuccess")
    public Result orderSuccess(@RequestBody @Validated OrderCoachRequest request, BindingResult bindingResult) {
        if (StringUtils.isEmpty(request.getUserId())){
            //userId从token获取
            String userId = UserUtils.getUserId();
            request.setUserId(userId);
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return coachOrderService.orderSuccess(request);
    }

    @Log("取消预约教练课程")
    @ApiOperation(value = "取消预约教练课程")
    @PostMapping("/cancelOrderTime")
    public Result cancelOrderTime(@RequestBody @Validated CancelOrderCoachRequest request, BindingResult bindingResult) {

        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (StringUtils.isEmpty(request.getUserId())){
            //userId从token获取
            String userId = UserUtils.getUserId();
            request.setUserId(userId);
        }

        return coachOrderService.cancelOrderTime(request.getCoachTimeId(),request.getUserId());
    }

    @Log("取消预约课程通过")
    @ApiOperation(value = "取消预约课程通过")
    @PostMapping("/cancelOrderSuccess")
    public Result cancelOrderSuccess(@RequestBody @Validated CancelOrderCoachRequest request, BindingResult bindingResult) {
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return coachOrderService.cancelOrderSuccess(request.getCoachTimeId(),request.getUserId());
    }

    @PostMapping("/coachOrder/closeUserCoachCourseOrder")
    @Log("关闭私教")
    @ApiOperation(value = "关闭私教", notes = "关闭私教")
    public Result closeUserCoachCourseOrder(@ApiParam("主键Id") @RequestParam(required = true, name = "id") String id) {
        return coachOrderService.closeUserCoachCourseOrder(id);
    }




}
