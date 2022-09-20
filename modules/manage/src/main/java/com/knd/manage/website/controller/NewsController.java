package com.knd.manage.website.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.website.request.ClassifyRequest;
import com.knd.manage.website.request.NewsRequest;
import com.knd.manage.website.service.INewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "云端管理-website")
@RestController
@CrossOrigin
@RequestMapping("/admin/website")
public class NewsController {

    @Resource
    private INewsService iNewsService;

    @Log("I26X1-新增更新新闻")
    @ApiOperation(value = "I26X1-新增更新新闻")
    @PostMapping("/saveNews")
    public Result saveNews(@RequestBody @Validated NewsRequest newsRequest, BindingResult bindingResult) {
        if(StringUtils.isEmpty(newsRequest.getUserId())){
            //userId从token获取
            newsRequest.setUserId(UserUtils.getUserId());
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }

        //新闻必须上传图片
        if (StringUtils.isEmpty(newsRequest.getPicAttach().getPicAttachName(),newsRequest.getPicAttach().getPicAttachNewName())){
            //参数校验失败
            return ResultUtil.error("U0995", "新闻必须上传封面图片");
        }
        //判断操作类型
        if (StringUtils.isEmpty(newsRequest.getId())) {
            //新增
            return iNewsService.add(newsRequest);
        } else {
            //更新
            return iNewsService.edit(newsRequest);
        }

    }


    @Log("I261-删除新闻")
    @ApiOperation(value = "I261-删除新闻")
    @PostMapping("/deleteNews")
    public Result deleteNews(@RequestBody @Validated NewsRequest newsRequest, BindingResult bindingResult) {
        if(StringUtils.isEmpty(newsRequest.getUserId())){
            //userId从token获取
            newsRequest.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iNewsService.delete(newsRequest);
    }

    @Log("I263-获取新闻")
    @ApiOperation(value = "I263-获取新闻")
    @GetMapping("/getNews")
    public Result getNews(@ApiParam("新闻id") @RequestParam(required = true,name = "id") String id) {
        //数据检查
        if (StringUtils.isEmpty(id)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iNewsService.getNews(id);
    }


    @Log("I260-获取新闻列表")
    @ApiOperation(value = "I260-获取新闻列表")
    @GetMapping("/getNewsList")
    public Result getNewsList(@ApiParam("分类") @RequestParam(required = false,name = "classify") String classify,
                              @ApiParam("是否推荐") @RequestParam(required = false,name = "recommend") String recommend,
                              @ApiParam("每页条数") @RequestParam(required = false,name = "size") Integer size,
                              @ApiParam("当前请求页") @RequestParam(required = true,name = "current") String current){
        if (StringUtils.isEmpty(current)){
            return ResultUtil.error("U0995", "当前请求页不能为空");
        }
        return iNewsService.getNewsList(classify,recommend,size, current);
    }



    @Log("I26X1-新增分类")
    @ApiOperation(value = "I26X1-新增分类")
    @PostMapping("/saveClassify")
    public Result saveClassify(@RequestBody @Validated ClassifyRequest classifyRequest, BindingResult bindingResult) {
        if(StringUtils.isEmpty(classifyRequest.getUserId())){
            //userId从token获取
            classifyRequest.setUserId(UserUtils.getUserId());
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        //判断操作类型
        if (StringUtils.isEmpty(classifyRequest.getId())) {
            //新增
            return iNewsService.addClassify(classifyRequest);
        } else {
            //更新
            return iNewsService.editClassify(classifyRequest);
        }

    }


    @Log("I261-删除分类")
    @ApiOperation(value = "I261-删除分类")
    @PostMapping("/deleteClassify")
    public Result deleteClassify(@RequestBody @Validated ClassifyRequest classifyRequest, BindingResult bindingResult) {
        if(StringUtils.isEmpty(classifyRequest.getUserId())){
            //userId从token获取
            classifyRequest.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iNewsService.deleteClassify(classifyRequest);
    }

    @Log("I263-获取分类列表")
    @ApiOperation(value = "I263-获取分类列表")
    @GetMapping("/getClassifyList")
    public Result getClassifyList() {

        return iNewsService.getClassifyList();
    }










}

