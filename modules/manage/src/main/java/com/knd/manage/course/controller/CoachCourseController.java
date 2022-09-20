package com.knd.manage.course.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.course.vo.VoGetCoachCourseList;
import com.knd.manage.course.vo.VoSaveCoach;
import com.knd.manage.course.service.CoachCourseService;
import com.knd.manage.course.service.CoachService;
import com.knd.manage.course.vo.VoGetCoachList;
import com.knd.manage.course.vo.VoSaveCoachCourse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 教练课程 管理
 * @author zm
 */
@Api(tags = "云端管理-basedata")
@RestController
@CrossOrigin
@RequestMapping("/admin/course")
@RequiredArgsConstructor
public class CoachCourseController {

    private final CoachService coachService;
    private final CoachCourseService coachCourseService;

    @Log("获取教练列表")
    @ApiOperation(value = "获取教练列表")
    @GetMapping("/getCoachList")
    public Result getCoachList(@Validated VoGetCoachList vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return coachService.getCoachList(vo);
    }

    @Log("获取教练信息")
    @ApiOperation(value = "获取教练信息")
    @GetMapping("/getCoach")
    public Result getCoach(String id) {
        return coachService.getCoach(id);
    }

    @Log("维护教练信息")
    @ApiOperation(value = "维护教练信息")
    @PostMapping("/updateCoach")
    public Result updateCoach(@RequestBody @Validated VoSaveCoach vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return coachService.edit(vo);
    }

    @Log("获取教练课程列表")
    @ApiOperation(value = "获取教练课程列表")
    @GetMapping("/getCoachCourseList")
    public Result getCoachCourseList(@Validated VoGetCoachCourseList vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return coachCourseService.getCoachCourseList(vo);
    }

    @Log("获取教练课程")
    @ApiOperation(value = "获取教练课程")
    @GetMapping("/getCoachCourse")
    public Result getCoachCourse(String id) {
        //数据检查
        if (StringUtils.isEmpty(id) || id.length() > 64) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return coachCourseService.getCoachCourse(id);
    }

    @Log("维护教练课程信息")
    @ApiOperation(value = "维护教练课程信息")
    @PostMapping("/saveCoachCourse")
    public Result saveCoachCourse(@RequestBody @Validated VoSaveCoachCourse vo, BindingResult bindingResult) {
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
        if (("1").equals(vo.getPostType())) {
            //新增
            return coachCourseService.add(vo);
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(vo.getCourseId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return coachCourseService.edit(vo);
        }
    }

    @Log("删除教练课程")
    @ApiOperation(value = "删除教练课程")
    @PostMapping("/deleteCoachCourse")
    public Result deleteCoachCourse(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return coachCourseService.delete(vo.getUserId(), vo.getId());
    }


}
