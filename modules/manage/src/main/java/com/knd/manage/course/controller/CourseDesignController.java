package com.knd.manage.course.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.course.request.AddTrainRequest;
import com.knd.manage.course.service.CourseDesignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author zm
 */
@Api(tags = "训练计划")
@RestController
@CrossOrigin
@RequestMapping("/admin/design")
@RequiredArgsConstructor
public class CourseDesignController {
    private final CourseDesignService courseDesignService;

    @Log("获取训练计划列表")
    @ApiOperation(value = "获取训练计划列表",notes = "获取训练计划列表")
    @GetMapping("/getTrainList")
    public Result getTrainList(@RequestParam(required = false) String programName,
                               @RequestParam(required = false) String type,
                               @RequestParam(required = false) String userId,
                               @RequestParam("current") @NotNull String current){
        return courseDesignService.getTrainList(programName,type,userId,current);
    }

    @Log("获取训练计划详情")
    @ApiOperation(value = "获取训练计划详情",notes = "获取训练计划详情")
    @GetMapping("/getTrainDetail")
    public Result getTrainDetail(@RequestParam("id") String id){
        return courseDesignService.getDetail(id);
    }

    @Log("删除训练计划")
    @ApiOperation(value = "删除训练计划",notes = "删除训练计划")
    @PostMapping("/deleteTrainProgram")
    public Result deleteTrainProgram(@RequestBody  @Validated VoId vo, BindingResult bindingResult){
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return courseDesignService.deleteTrain(vo.getId());
    }

    @Log("获取用户动作组合列表")
    @ApiOperation(value = "获取用户动作组合列表", notes = "获取用户动作组合列表")
    @GetMapping("/getActionArray")
    public Result getActionArray(@RequestParam(required = false) String actionArrayName,
                                     @RequestParam("current") @NotNull String current) {
        return courseDesignService.getActionList(actionArrayName,current);
    }

    @Log("维护训练计划")
    @ApiOperation(value = "维护训练计划",notes = "维护训练计划")
    @PostMapping("/saveTrainProgram")
    public Result  saveTrainProgram(@RequestBody @Valid AddTrainRequest request, BindingResult bindingResult){
        if(StringUtils.isEmpty(request.getUserId())){
            //userId从token获取
            request.setUserId(UserUtils.getUserId());
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        //判断操作类型
        if (request.getPostType().equals("1")) {
            //新增
            return courseDesignService.addTrain(request);
        } else {
            //更新
            //数据检查
            if (StringUtils.isEmpty(request.getProgramId())) {
                //参数校验失败
                return ResultUtil.error(ResultEnum.PARAM_ERROR);
            }
            return courseDesignService.editTrain(request);
        }

    }
}
