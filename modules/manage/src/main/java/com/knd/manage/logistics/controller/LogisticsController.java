
package com.knd.manage.logistics.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.logistics.service.ILogisticsService;
import com.knd.manage.logistics.vo.VoLogistics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "云端管理-物流Logistics")
@RestController
@CrossOrigin
@RequestMapping("/admin/logistics")
@RequiredArgsConstructor
public class LogisticsController {

    @Resource
    private ILogisticsService iLogisticsService;


    @Log("I252-新标准轨迹查询-1德邦,2安能")
    @ApiOperation(value = "I252-新标准轨迹查询-1德邦,2安能")
    @PostMapping("/trajectoryQuery")
    public Result trajectoryQuery(@RequestBody @Validated VoLogistics vo , BindingResult bindingResult) {

        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
            return iLogisticsService.trajectoryQuery(vo);
    }

    @Log("I252-订单查询-德邦")
    @ApiOperation(value = "I252-订单查询-德邦")
    @PostMapping("/queryOrder")
    public Result queryOrder(@ApiParam("渠道单号 渠道单号 String 32 是 由第三⽅接⼊商产⽣的订单号（⽣成规则为sign+数字，sign值由双⽅约定）")@RequestParam(required = false) String logisticID,
                             @ApiParam("物流公司ID 默认DEPPON")@RequestParam(required = false,defaultValue = "DEPPON") String logisticCompanyID) {
        return iLogisticsService.queryOrder(logisticID,logisticCompanyID);
    }







}

