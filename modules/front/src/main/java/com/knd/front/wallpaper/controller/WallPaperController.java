package com.knd.front.wallpaper.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.front.wallpaper.request.DelUserWallPaperRequest;
import com.knd.front.wallpaper.request.DelWallPaperRequest;
import com.knd.front.wallpaper.request.SaveUserWallPaperRequest;
import com.knd.front.wallpaper.request.SaveWallPaperRequest;
import com.knd.front.wallpaper.service.IWallPaperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "自定义壁纸wallPaper")
@RestController
@CrossOrigin
@RequestMapping("/front/wallPaper")
@RequiredArgsConstructor
public class WallPaperController {

    @Resource
    private IWallPaperService iWallPaperService;

    @Log("I26X1-新增自定义和系统壁纸")
    @ApiOperation(value = "I26X1-新增自定义和系统壁纸")
    @PostMapping("/saveWallPaper")
    public Result saveWallPaper(@RequestBody @Validated SaveWallPaperRequest wallPaperRequest, BindingResult bindingResult) {
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        //系统壁纸必须上传图片
        if (!(StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachName())
                && StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachNewName())
                && StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachSize()))) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(), "自定义壁纸必须上传图片");
        }
        //新增
        return iWallPaperService.add(wallPaperRequest);

    }

/*
    @Log("I26X1-更新自定义壁纸")
    @ApiOperation(value = "I26X1-更新自定义壁纸")
    @PostMapping("/updateWallPaper")
    public Result updateWallPaper(@RequestBody @Validated UpdateWallPaperRequest wallPaperRequest, BindingResult bindingResult) {
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        //系统壁纸必须上传图片
        if (!(StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachName())
                && StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachNewName())
                && StringUtils.isNotEmpty(wallPaperRequest.getPicAttachUrl().getPicAttachSize()))) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(), "系统壁纸必须上传图片");
        }

        //更新
        return iWallPaperService.edit(wallPaperRequest);
    }
*/


    @Log("I26X2-删除自定义壁纸")
    @ApiOperation(value = "I26X2-删除自定义壁纸")
    @PostMapping("/deleteWallPaper")
    public Result deleteWallPaper(@RequestBody @Validated DelWallPaperRequest delWallPaperRequest, BindingResult bindingResult) {
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iWallPaperService.delete(delWallPaperRequest.getIds());
    }

 /*   @Log("I263-获取壁纸")
    @ApiOperation(value = "I263-获取壁纸")
    @GetMapping("/getWallPaper")
    public Result getWallPaper(@ApiParam("壁纸id") @RequestParam(required = true, name = "id") String id) {
        //数据检查
        if (StringUtils.isEmpty(id)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iWallPaperService.getWallPaper(id);
    }
*/

    @Log("I26X3-获取壁纸列表 type=1系统壁纸 type=2自定义壁纸 type=3运动数据壁纸")
    @ApiOperation(value = "I26X3-获取壁纸列表 type=1系统壁纸 type=2自定义壁纸 type=3运动数据壁纸")
    @GetMapping("/getWallPaperList")
    public Result getWallPaperList(
                                   @ApiParam("类型 1系统壁纸 2自定义壁纸 3运动数据壁纸") @RequestParam(required = false, name = "type") String type,
                                   @ApiParam("每页条数") @RequestParam(required = false, name = "size") Integer size,
                                   @ApiParam("当前请求页") @RequestParam(required = true, name = "current") String current) {
        if (StringUtils.isEmpty(current)) {
            return ResultUtil.error("U0995", "当前请求页不能为空");
        }
        return iWallPaperService.getWallPaperList(type, size, current);
    }

    @Log("I26X4-设置我的壁纸")
    @ApiOperation(value = "I26X4-设置我的壁纸")
    @PostMapping("/saveUserWallPaper")
    public Result saveUserWallPaper(@RequestBody @Validated SaveUserWallPaperRequest saveUserWallPaperRequest, BindingResult bindingResult) {

        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        //新增
        return iWallPaperService.saveUserWallPaper(saveUserWallPaperRequest);
    }

/*    @Log("I26X5-更新我的壁纸")
    @ApiOperation(value = "I26X5-更新我的壁纸")
    @PostMapping("/updateUserWallPaper")
    public Result updateUserWallPaper(@RequestBody @Validated UpdateUserWallPaperRequest updateUserWallPaperRequest, BindingResult bindingResult) {
        if(StringUtils.isEmpty(updateUserWallPaperRequest.getUserId())){
            //userId从token获取
            updateUserWallPaperRequest.setUserId(UserUtils.getUserId());
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        //新增
        return iWallPaperService.updateUserWallPaper(updateUserWallPaperRequest);
    }*/

  @Log("I26X6-删除我的壁纸")
    @ApiOperation(value = "I26X6-删除我的壁纸")
    @PostMapping("/deleteUserWallPaper")
    public Result deleteUserWallPaper(@RequestBody @Validated DelUserWallPaperRequest delUserWallPaperRequest, BindingResult bindingResult) {
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iWallPaperService.deleteUserWallPaper(delUserWallPaperRequest.getIds());
    }

   @Log("I26X7-获取我的壁纸列表")
    @ApiOperation(value = "I26X7-获取我的壁纸列表")
    @GetMapping("/getUserWallPaperList")
    public Result getUserWallPaperList(
                                   @ApiParam("类型 1系统壁纸 2自定义壁纸 3运动数据壁纸 不传全部") @RequestParam(required = false, name = "type") String type,
                                   @ApiParam("每页条数") @RequestParam(required = false, name = "size") Integer size,
                                   @ApiParam("当前请求页") @RequestParam(required = true, name = "current") String current) {
        if (StringUtils.isEmpty(current)) {
            return ResultUtil.error("U0995", "当前请求页不能为空");
        }
        return iWallPaperService.getUserWallPaperList(type, size, current);
    }











}

