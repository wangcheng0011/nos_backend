package com.knd.manage.wallpaper.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.manage.wallpaper.request.WallPaperDelRequest;
import com.knd.manage.wallpaper.request.WallPaperRequest;
import com.knd.manage.wallpaper.service.IWallPaperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "云端管理-系统壁纸")
@RestController
@CrossOrigin
@RequestMapping("/sys/wallPaper")
@RequiredArgsConstructor
public class WallPaperController {

    @Resource
    private IWallPaperService iWallPaperService;

    @Log("I26X1-新增更新系统壁纸或运动数据壁纸")
    @ApiOperation(value = "I26X1-新增更新系统壁纸或运动数据壁纸")
    @PostMapping("/saveUpdateWallPaper")
    public Result saveUpdateWallPaper(@RequestBody @Validated WallPaperRequest wallPaperRequest, BindingResult bindingResult) {
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
            return ResultUtil.error(ResultEnum.PARAM_ERROR.getCode(), "壁纸必须上传图片");
        }
        //判断操作类型
        if ("1".equals(wallPaperRequest.getPostType())) {
            //新增
            return iWallPaperService.add(wallPaperRequest);
        } else {
            //更新
            return iWallPaperService.edit(wallPaperRequest);
        }


    }


    @Log("I261-删除壁纸")
    @ApiOperation(value = "I261-删除系统壁纸")
    @PostMapping("/deleteWallPaper")
    public Result deleteWallPaper(@RequestBody @Validated WallPaperDelRequest wallPaperDelRequest, BindingResult bindingResult) {
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iWallPaperService.delete(wallPaperDelRequest.getId());
    }

    @Log("I263-获取系统壁纸")
    @ApiOperation(value = "I263-获取系统壁纸")
    @GetMapping("/getWallPaper")
    public Result getWallPaper(@ApiParam("壁纸id") @RequestParam(required = true, name = "id") String id) {
        //数据检查
        if (StringUtils.isEmpty(id)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iWallPaperService.getWallPaper(id);
    }


    @Log("I260-获取壁纸列表 type=1系统壁纸 type=2自定义壁纸 type=3运动数据壁纸")
    @ApiOperation(value = "I260-获取壁纸列表 type=1系统壁纸 type=3运动数据壁纸")
    @GetMapping("/getWallPaperList")
    public Result getWallPaperList(@ApiParam("名称") @RequestParam(required = false, name = "name") String name,
                                   @ApiParam("类型 1系统壁纸 2自定义壁纸 3运动数据壁纸") @RequestParam(required = false, name = "type") String type,
                                   @ApiParam("每页条数") @RequestParam(required = false, name = "size") Integer size,
                                   @ApiParam("当前请求页") @RequestParam(required = true, name = "current") String current) {
        if (StringUtils.isEmpty(current)) {
            return ResultUtil.error("U0995", "当前请求页不能为空");
        }
        return iWallPaperService.getWallPaperList(name, type, size, current);
    }


}

