package com.knd.manage.basedata.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.basedata.service.BaseFrequencyService;
import com.knd.manage.basedata.vo.VoSaveFrequency;
import com.knd.manage.common.vo.VoId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zm
 */
@Api(tags = "云端管理-basedata")
@RestController
@CrossOrigin
@RequestMapping("/admin/basedata")
@RequiredArgsConstructor
public class BaseFrequencyController {

    private final BaseFrequencyService baseFrequencyService;

    @Log("I230-获取运动频率列表")
    @ApiOperation(value = "I230-获取运动频率列表")
    @GetMapping("/getFrequencyList")
    public Result getFrequencyList(@ApiParam(value = "运动频率名称") @RequestParam(required = false) String frequency,
                                   @ApiParam(value = "当前页") @RequestParam(required = false) String current) {
        return baseFrequencyService.getFrequencyList(frequency,current);
    }

    @Log("I230-获取运动频率")
    @ApiOperation(value = "I230-获取运动频率")
    @GetMapping("/getFrequency")
    public Result getFrequency(@ApiParam(value = "id") @RequestParam(required = true) String id){
        return baseFrequencyService.getFrequency(id);
    }

    @Log("I230-删除运动频率")
    @ApiOperation(value = "I230-删除运动频率")
    @PostMapping("/deleteFrequency")
    public Result deleteFrequency(@RequestBody  @Validated VoId vo, BindingResult bindingResult){
        //userId从token获取
        if (StringUtils.isEmpty(vo.getUserId())){
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseFrequencyService.deleteFrequency(vo.getUserId(),vo.getId());
    }

    @Log("I232-维护运动频率")
    @ApiOperation(value = "I232-维护运动频率")
    @PostMapping("/saveFrequency")
    public Result saveFrequency(@RequestBody  @Validated VoSaveFrequency vo,BindingResult bindingResult){
        //userId从token获取
        if (StringUtils.isEmpty(vo.getUserId())){
            vo.setUserId(UserUtils.getUserId());
        }

        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if (vo.getPostType().equals("1")) {
            //新增
            return baseFrequencyService.add(vo);
        }else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getFrequencyId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return baseFrequencyService.edit(vo);
        }
    }

}
