package com.knd.front.help.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.front.help.request.HelpRequest;
import com.knd.front.help.service.IHelpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "帮助-help")
@RestController
@CrossOrigin
@RequestMapping("/front/help")
public class HelpController {

    @Resource
    private IHelpService iHelpService;

    @Log("I26X1-新增更新帮助")
    @ApiOperation(value = "I26X1-新增更新帮助")
    @PostMapping("/saveHelp")
    @Deprecated
    public Result saveHelp(@RequestBody @Validated HelpRequest helpRequest, BindingResult bindingResult) {
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }

        //帮助必须上传图片
        if (helpRequest.getImageUrls().size()==0){
            //参数校验失败
            return ResultUtil.error("U0995", "帮助必须上传图片");
        }
        //判断操作类型
        if ("1".equals(helpRequest.getPostType())) {
            //新增
            return iHelpService.add(helpRequest);
        } else {
            //更新
            return iHelpService.edit(helpRequest);
        }

    }


    @Log("I261-删除帮助")
    @ApiOperation(value = "I261-删除帮助")
    @PostMapping("/deleteHelp")
    @Deprecated
    public Result deleteHelp(@RequestBody @Validated HelpRequest helpRequest, BindingResult bindingResult) {
        return iHelpService.delete(helpRequest);
    }

    @Log("I263-获取帮助")
    @ApiOperation(value = "I263-获取帮助")
    @GetMapping("/getHelp")
    public Result getHelp(@ApiParam("帮助id") @RequestParam(required = true,name = "id") String id) {
        //数据检查
        if (StringUtils.isEmpty(id)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iHelpService.getHelp(id);
    }


    @Log("I260-获取帮助列表")
    @ApiOperation(value = "I260-获取帮助列表")
    @GetMapping("/getHelpList")
    public Result getHelpList(@ApiParam("标题") @RequestParam(required = false,name = "title") String title,
                              @ApiParam("每页条数") @RequestParam(required = false,name = "size") Integer size,
                              @ApiParam("当前请求页") @RequestParam(required = true,name = "current") String current){
        if (StringUtils.isEmpty(current)){
            return ResultUtil.error("U0995", "当前请求页不能为空");
        }
        return iHelpService.getHelpList(title,size,current);
    }












}

