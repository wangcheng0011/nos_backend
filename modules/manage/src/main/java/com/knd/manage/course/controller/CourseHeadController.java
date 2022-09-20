package com.knd.manage.course.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.course.service.ICourseHeadService;
import com.knd.manage.course.vo.VoSaveCourseHead;
import com.knd.manage.course.vo.VoSaveCourseReleaseFlag;
import com.knd.manage.course.vo.VoSaveCourseVedioDuration;
import com.knd.manage.course.vo.VoUpdateCoursesSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Api(tags = "云端管理-course")
@RestController
@CrossOrigin
@RequestMapping("/admin/course")
public class CourseHeadController {

    @Resource
    private ICourseHeadService iCourseHeadService;


    @Log("I270-维护课程管理")
    @ApiOperation(value = "I270-维护课程管理")
    @PostMapping("/saveCourseHead")
    public Result saveCourseHead(@RequestBody @Validated VoSaveCourseHead vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //判断结果
        if (bindingResult.hasErrors()) {
            //参数校验失败
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        if (vo.getTrainingFlag().equals("1") && (vo.getTargetList().isEmpty() || vo.getPartList().isEmpty())) {
            //参数校验失败
            return ResultUtil.error("U0995","训练视频，目标和部位不可空");
        }
        //判断操作类型
        if (vo.getPostType().equals("1")) {
            //新增
            return iCourseHeadService.add(vo);
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return iCourseHeadService.edit(vo);
        }
    }


    @Log("I271-设置课程发布状态")
    @ApiOperation(value = "I271-设置课程发布状态")
    @PostMapping("/saveCourseReleaseFlag")
    public Result saveCourseReleaseFlag(@RequestBody @Validated VoSaveCourseReleaseFlag vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //判断结果
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iCourseHeadService.saveCourseReleaseFlag(vo);
    }

    @Log("I274-获取课程")
    @ApiOperation(value = "I274-获取课程")
    @GetMapping("/getCourseHead")
    public Result getCourseHead(String id) {
        if (StringUtils.isEmpty(id) && id.length() <= 64) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iCourseHeadService.getCourseHead(id);
    }
    @Log("I273-删除课程")
    @ApiOperation(value = "I273-删除课程")
    @PostMapping("/deleteCourse")
    public Result deleteCourse(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        return iCourseHeadService.deleteCourse(vo.getUserId(), vo.getId());
    }

    @Log("I272-获取课程列表")
    @ApiOperation("I272-获取课程列表")
    @GetMapping("/getCourseHeadList")
    public Result getCourseHeadList(String course, String typeid, String targetid, String partid, String releaseFlag, String courseType,String current) {
        return iCourseHeadService.getCourseHeadList(course, typeid, targetid, partid, releaseFlag,courseType, current);
    }

    @Log("I275-批量更新")
    @ApiOperation(value = "I275-批量更新")
    @PostMapping("/updateCoursesSort")
    public Result updateCoursesSort(@Validated @RequestBody VoUpdateCoursesSort vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iCourseHeadService.updateCoursesSort(vo.getUserId(), vo.getCourseSortList());
    }

    @Log("I280-获取课程视频信息")
    @ApiOperation(value = "I280-获取课程视频信息")
    @GetMapping("/getCourseVedioInfo")
    public Result getCourseVedioInfo(String id) {
        if (StringUtils.isEmpty(id)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iCourseHeadService.getCourseVedioInfo(id);
    }


    @Log("I281-维护视频时长")
    @ApiOperation(value = "I281-维护视频时长")
    @PostMapping("/saveCourseVedioDuration")
    public Result saveCourseVedioDuration(@RequestBody @Validated VoSaveCourseVedioDuration vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        try {
            //三位数时，去掉前面无意义的0
            if (vo.getVideoDurationMinutes().length()>2){
                vo.setVideoDurationMinutes(Integer.parseInt(vo.getVideoDurationMinutes())+"");
                if (vo.getVideoDurationMinutes().length()>8){
                    return ResultUtil.error("U0995", "分钟最大8位数");
                }
            }
            if (vo.getVideoDurationSeconds().length()>2){
                vo.setVideoDurationSeconds(Integer.parseInt(vo.getVideoDurationSeconds())+"");
                if (vo.getVideoDurationSeconds().length()>8){
                    return ResultUtil.error("U0995", "秒最大8位数");
                }
            }
        } catch (Exception e) {
            //参数校验失败
            return ResultUtil.error("U0995", "视频总时长参数格式错误");
        }
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        return iCourseHeadService.saveCourseVedioDuration(vo);
    }


}

