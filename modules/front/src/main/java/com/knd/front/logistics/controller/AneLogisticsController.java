
package com.knd.front.logistics.controller;


import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.front.logistics.service.IAneLogisticsService;
import com.knd.front.logistics.vo.VoAneLogistics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@Api(tags = "安能物流AneLogistics")
@RestController
@CrossOrigin
@RequestMapping("/front/aneLogistics")
@RequiredArgsConstructor
public class AneLogisticsController {

    @Resource
    private IAneLogisticsService iAneLogisticsService;


    @Log("I252-轨迹查询接口-安能")
    @ApiOperation(value = "I252-货物跟踪查询-安能")
    @PostMapping("/trajectoryQuery")
    @Deprecated
    public Result trajectoryQuery(@RequestBody @Validated VoAneLogistics vo , BindingResult bindingResult) {
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
            return iAneLogisticsService.trajectoryQuery(vo);
    }









}

