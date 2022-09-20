package com.knd.manage.basedata.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.basedata.service.IbasePageService;
import com.knd.manage.basedata.vo.VoGetPageList;
import com.knd.manage.basedata.vo.VoSavePage;
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
public class BasePageController {

    private final IbasePageService basePageService;


    @Log("I210-获取页面列表")
    @ApiOperation(value = "I210-获取页面列表")
    @GetMapping("/getPageList")
    public Result getPageList(@Validated VoGetPageList vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return basePageService.getPageList(vo);
    }

    @Log("I213-获取页面详情")
    @ApiOperation(value = "I213-获取页面详情")
    @GetMapping("/GetPage")
    public Result GetPage(String id) {
        //数据检查
        if (StringUtils.isEmpty(id) || id.length() > 64) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return basePageService.getPage(id);
    }

    @Log("I211-删除页面")
    @ApiOperation(value = "I211-删除页面")
    @PostMapping("/deletePage")
    public Result deletePage(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return basePageService.deletePage(vo.getUserId(), vo.getId());
    }

    @Log("I212-维护页面")
    @ApiOperation(value = "I212-维护页面")
    @PostMapping("/savePage")
    public Result savePage(@RequestBody @Validated VoSavePage vo, BindingResult bindingResult) {
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
            return basePageService.addPage(vo);
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getPageId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return basePageService.editPage(vo);
        }
    }


}
