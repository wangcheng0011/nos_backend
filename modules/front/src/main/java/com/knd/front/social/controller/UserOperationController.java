package com.knd.front.social.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.social.request.*;
import com.knd.front.social.service.UserOperationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * 用户操作
 */
@Api(tags = "社交social")
@RestController
@CrossOrigin
@RequestMapping("/front/social")
@RequiredArgsConstructor
public class UserOperationController {

    private final UserOperationService userOperationService;

    @Log("发布动态")
    @PostMapping("/moment")
    @ApiOperation(value = "发布动态", notes = "发布动态")
    public Result moment(@RequestBody OperationMomentRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return userOperationService.moment(request);
    }

    @Log("动态点赞或取消点赞")
    @PostMapping("/praise")
    @ApiOperation(value = "动态点赞或取消点赞", notes = "动态点赞或取消点赞")
    public Result praise(@RequestBody OperationPraiseRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return userOperationService.praise(request);
    }

    @Log("动态回复信息")
    @PostMapping("/comment")
    @ApiOperation(value = "动态回复信息", notes = "动态回复信息")
    public Result comment(@RequestBody OperationCommentRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return userOperationService.comment(request);
    }

    @Log("关注，取关，回关，移除")
    @PostMapping("/followOrPass")
    @ApiOperation(value = "关注，取关，回关，移除", notes = "关注，取关，回关，移除")
    public Result followOrPass(@RequestBody OperationFollowRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return userOperationService.followOrPass(request);
    }

    @Log("删除照片")
    @PostMapping("/deletePic")
    @ApiOperation(value = "删除照片", notes = "删除照片")
    public Result deletePic(@RequestBody OperationDeletePicRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return userOperationService.deletePic(request);
    }

    @Log("上传照片")
    @PostMapping("/upPic")
    @ApiOperation(value = "上传照片", notes = "上传照片")
    public Result upPic(@RequestBody OperationUpPicRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return userOperationService.upPic(request);
    }


}
