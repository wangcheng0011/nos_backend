package com.knd.manage.course.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.course.service.SeriesCourseService;
import com.knd.manage.course.vo.VoSaveSeries;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author zm
 */
@Api(tags = "系列课程")
@RestController
@CrossOrigin
@RequestMapping("/admin/series")
@RequiredArgsConstructor
public class SeriesCourseController {

    private final SeriesCourseService seriesCourseService;

    @Log("获取系列课程列表")
    @ApiOperation(value = "获取系列课程列表",notes = "获取系列课程列表")
    @GetMapping("/getSeriesCourseList")
    public Result getSeriesCourseList(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String userId,
                               @RequestParam("current") @NotNull String current){
        return seriesCourseService.getList(name,userId,current);
    }

    @Log("获取系列课程详情")
    @ApiOperation(value = "获取系列课程详情",notes = "获取系列课程详情")
    @GetMapping("/getSeriesCourseDetail")
    public Result getSeriesCourseDetail(@RequestParam("id") String id){
        return seriesCourseService.getDetail(id);
    }

    @Log("删除取系列课程")
    @ApiOperation(value = "删除取系列课程",notes = "删除取系列课程")
    @PostMapping("/deleteSeriesCourse")
    public Result deleteSeriesCourse(@RequestBody  @Validated VoId vo, BindingResult bindingResult){
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return seriesCourseService.delete(vo);
    }

    @Log("I212-维护系列课程")
    @ApiOperation(value = "I212-维护系列课程")
    @PostMapping("/saveSeriesCourse")
    public Result saveSeriesCourse(@RequestBody @Validated VoSaveSeries vo, BindingResult bindingResult) {
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
            return seriesCourseService.add(vo);
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getSeriesId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return seriesCourseService.edit(vo);
        }
    }


}
