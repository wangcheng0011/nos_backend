package com.knd.manage.basedata.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.basedata.service.IBaseDifficultyService;
import com.knd.manage.basedata.vo.VoGetDifficultyList;
import com.knd.manage.basedata.vo.VoSaveDifficulty;
import com.knd.manage.common.vo.VoId;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "云端管理-basedata")
@RestController
@CrossOrigin
@RequestMapping("/admin/basedata")
@RequiredArgsConstructor
public class BaseDifficultyController {

    private final IBaseDifficultyService baseDifficultyService;

    @Log("I210-获取难度列表")
    @ApiOperation(value = "I210-获取难度列表")
    @GetMapping("/getDifficultyList")
    public Result getDifficultyList(@Validated VoGetDifficultyList vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseDifficultyService.getDifficultyList(vo);
    }

    @Log("I212-维护难度")
    @ApiOperation(value = "I212-维护难度")
    @PostMapping("/saveDifficulty")
    public Result saveDifficulty(@RequestBody @Validated VoSaveDifficulty vo, BindingResult bindingResult) {
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
            return baseDifficultyService.add(vo.getUserId(), vo);
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getDifficultyId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return baseDifficultyService.edit(vo.getUserId(), vo);
        }

    }

    @Log("I211-删除难度")
    @ApiOperation(value = "I211-删除难度")
    @PostMapping("/deleteDifficulty")
    public Result deleteDifficulty(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseDifficultyService.delete(vo.getUserId(), vo.getId());
    }


    @Log("I213-获取难度")
    @ApiOperation(value = "I213-获取难度")
    @GetMapping("/GetDifficulty")
    public Result getDifficulty(String id) {
        //数据检查
        if (StringUtils.isEmpty(id) || id.length() > 64) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return baseDifficultyService.getDifficulty(id);
    }


}
