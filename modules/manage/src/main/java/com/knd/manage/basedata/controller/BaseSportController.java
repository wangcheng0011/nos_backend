package com.knd.manage.basedata.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.basedata.service.BaseSportService;
import com.knd.manage.basedata.vo.VoSaveSport;
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
public class BaseSportController {

    private final BaseSportService baseSportService;

    @Log("I230-获取运动方式列表")
    @ApiOperation(value = "I230-获取运动方式列表")
    @GetMapping("/getSportList")
    public Result getSportList(@ApiParam(value = "运动方式名称") @RequestParam(required = false) String sport,
                                   @ApiParam(value = "当前页") @RequestParam(required = false) String current) {
        return baseSportService.getSportList(sport,current);
    }

    @Log("I230-获取运动方式")
    @ApiOperation(value = "I230-获取运动方式")
    @GetMapping("/getSport")
    public Result getSport(@ApiParam(value = "id") @RequestParam(required = true) String id){
        return baseSportService.getSport(id);
    }

    @Log("I230-删除运动方式")
    @ApiOperation(value = "I230-删除运动方式")
    @PostMapping("/deleteSport")
    public Result deleteSport(@RequestBody  @Validated VoId vo, BindingResult bindingResult){
        //userId从token获取
        if (StringUtils.isEmpty(vo.getUserId())){
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseSportService.deleteSport(vo.getUserId(),vo.getId());
    }

    @Log("I232-维护运动方式")
    @ApiOperation(value = "I232-维护运动方式")
    @PostMapping("/saveSport")
    public Result saveSport(@RequestBody  @Validated VoSaveSport vo,BindingResult bindingResult){
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
            return baseSportService.add(vo);
        }else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getSportId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return baseSportService.edit(vo);
        }
    }

}
