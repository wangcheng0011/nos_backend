package com.knd.manage.course.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.course.service.IBaseCourseTypeService;
import com.knd.manage.course.vo.VoSaveCourseType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "云端管理-course")
@RestController
@CrossOrigin
@RequestMapping("/admin/course")
@RequiredArgsConstructor
public class BaseCourseTypeController {


    private final IBaseCourseTypeService iBaseCourseTypeService;

    @Log("I252-维护课程分类")
    @ApiOperation(value = "I252-维护课程分类")
    @PostMapping("/saveCourseType")
    public Result saveCourseType(@RequestBody @Validated VoSaveCourseType vo , BindingResult bindingResult) {
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
            return iBaseCourseTypeService.add(vo.getUserId(), vo.getType(), vo.getRemark(), vo.getAppHomeFlag(), vo.getSort());
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getTypeId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return iBaseCourseTypeService.edit(vo.getUserId(), vo.getType(), vo.getRemark(), vo.getAppHomeFlag(), vo.getSort(), vo.getTypeId());
        }
    }



    @Log("I251-删除课程分类")
    @ApiOperation(value = "I251-删除课程分类")
    @PostMapping("/deleteCourseType")
    public Result deleteCourseType(@RequestBody  @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iBaseCourseTypeService.deleteCourseType(vo.getUserId(), vo.getId());
    }

    @Log("I253-获取课程分类")
    @ApiOperation(value = "I253-获取课程分类")
    @GetMapping("/getCourseType")
    public Result getCourseType(String id) {
        //数据检查
        if (StringUtils.isEmpty(id)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iBaseCourseTypeService.getCourseType(id);
    }
    @Log("I250-获取课程分类列表")
    @ApiOperation(value = "I250-获取课程分类列表")
    @GetMapping("/getCourseTypeList")
    public Result getCourseTypeList(String type, String current) {
        return iBaseCourseTypeService.getCourseTypeList(type, current);
    }


}

