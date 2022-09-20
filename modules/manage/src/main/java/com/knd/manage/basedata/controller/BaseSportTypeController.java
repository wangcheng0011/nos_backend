package com.knd.manage.basedata.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.basedata.service.impl.BaseSportTypeServiceImpl;
import com.knd.manage.basedata.vo.VoSaveSportType;
import com.knd.manage.common.vo.VoId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangcheng
 */
@Api(tags = "云端管理-basedata")
@RestController
@CrossOrigin
@RequestMapping("/admin/basedata")
@RequiredArgsConstructor
public class BaseSportTypeController {

    private final BaseSportTypeServiceImpl baseSportTypeService;

    @Log("I230-获取运动类型列表")
    @ApiOperation(value = "I230-获取运动类型列表")
    @GetMapping("/getSportTypeList")
    public Result getSportTypeList(@ApiParam(value = "运动类型") @RequestParam(required = false) String type,
                                   @ApiParam(value = "当前页") @RequestParam(required = false) String current) {
        return baseSportTypeService.getSportTypeList(type,current);
    }

    @Log("I230-获取运动类型")
    @ApiOperation(value = "I230-获取运动类型")
    @GetMapping("/getSportType")
    public Result getSportType(@ApiParam(value = "id") @RequestParam(required = true) String id){
        return baseSportTypeService.getSportType(id);
    }

    @Log("I230-删除运动类型")
    @ApiOperation(value = "I230-删除运动类型")
    @PostMapping("/deleteSportType")
    public Result deleteSportType(@RequestBody  @Validated VoId vo, BindingResult bindingResult){
        //userId从token获取
        if (StringUtils.isEmpty(vo.getUserId())){
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseSportTypeService.deleteSportType(vo.getUserId(),vo.getId());
    }

    @Log("I232-维护运动类型 ")
    @ApiOperation(value = "I232-维护运动类型")
    @PostMapping("/saveSportType")
    public Result saveSportType(@RequestBody  @Validated VoSaveSportType vo, BindingResult bindingResult){
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
            return baseSportTypeService.add(vo);
        }else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getTypeId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return baseSportTypeService.edit(vo);
        }
    }

}
