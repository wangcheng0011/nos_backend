package com.knd.manage.basedata.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.basedata.service.IBaseBodyPartService;
import com.knd.manage.basedata.vo.VoGetPartList;
import com.knd.manage.basedata.vo.VoSavePart;
import com.knd.manage.common.vo.VoId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "云端管理-basedata")
@RestController
@CrossOrigin
@RequestMapping("/admin/basedata")
public class BaseBodyPartController {

    @Resource
    private IBaseBodyPartService iBaseBodyPartService;

    @Log("I212-维护部位")
    @ApiOperation(value = "I212-维护部位")
    @PostMapping("/savePart")
    public Result savePart(@RequestBody @Validated VoSavePart vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        //判断操作类型
        if (vo.getPostType().equals("1")) {
            //新增
            return iBaseBodyPartService.add(vo.getUserId(), vo.getPart(), vo.getRemark());
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getPartId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return iBaseBodyPartService.edit(vo.getUserId(), vo.getPart(), vo.getRemark(), vo.getPartId());
        }

    }

    @Log("I211-删除部位")
    @ApiOperation(value = "I211-删除部位")
    @PostMapping("/deletePart")
    public Result deletePart(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iBaseBodyPartService.deletePart(vo.getUserId(), vo.getId());
    }


    @Log("I213-获取部位")
    @ApiOperation(value = "I213-获取部位")
    @GetMapping("/GetPart")
    public Result GetPart(String id) {
        //数据检查
        if (StringUtils.isEmpty(id) || id.length() > 64) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iBaseBodyPartService.GetPart(id);
    }

    @Log("I210-获取部位列表")
    @ApiOperation(value = "I210-获取部位列表")
    @GetMapping("/getPartList")
    public Result getPartList(@Validated VoGetPartList vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iBaseBodyPartService.getPartList(vo.getPart(), vo.getCurrent());
    }


}
