package com.knd.manage.course.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.course.service.ICourseTrainningNodeInfoService;
import com.knd.manage.course.vo.VoSaveCourseNodeInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "云端管理-course")
@RestController
@CrossOrigin
@RequestMapping("/admin/course")
@Slf4j
@RequiredArgsConstructor
public class CourseTrainningNodeInfoController {

    private final ICourseTrainningNodeInfoService iCourseTrainningNodeInfoService;


    @Log("I287-维护训练课小节信息")
    @ApiOperation(value = "I287-维护训练课小节信息")
    @PostMapping("/saveCourseNodeInfo")
    public Result saveCourseNodeInfo(@RequestBody @Validated VoSaveCourseNodeInfo vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        if (bindingResult.hasErrors()) {
            //参数校验失败
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        if (vo.getNodeBeginMinutes().contains(".")
                || vo.getNodeEndMinutes().contains(".")
                || vo.getNodeBeginSeconds().contains(".")
                || vo.getNodeEndSeconds().contains(".")) {
            return ResultUtil.error("U0995", "时间不允许输入 . 符号");
        }
        //数字转换
        try {
            //三位数时，去掉前面无意义的0
            if (vo.getNodeBeginMinutes().length()>2){
                vo.setNodeBeginMinutes(Integer.parseInt(vo.getNodeBeginMinutes())+"");
                if (vo.getNodeBeginMinutes().length()>8){
                    return ResultUtil.error("U0995", "开始时间-分最大8位数");
                }
            }
            if (vo.getNodeBeginSeconds().length()>2){
                vo.setNodeBeginSeconds(Integer.parseInt(vo.getNodeBeginSeconds())+"");
                if (vo.getNodeBeginSeconds().length()>8){
                    return ResultUtil.error("U0995", "开始时间-秒最大8位数");
                }
            }
            if (vo.getNodeEndMinutes().length()>2){
                vo.setNodeEndMinutes(Integer.parseInt(vo.getNodeEndMinutes())+"");
                if (vo.getNodeEndMinutes().length()>8){
                    return ResultUtil.error("U0995", "结束时间-分最大8位数");
                }
            }
            if (vo.getNodeEndSeconds().length()>2){
                vo.setNodeEndSeconds(Integer.parseInt(vo.getNodeEndSeconds())+"");
                if (vo.getNodeEndSeconds().length()>8){
                    return ResultUtil.error("U0995", "结束时间-秒最大8位数");
                }
            }

        } catch (Exception e) {
            //参数校验失败
            return ResultUtil.error("U0995", "时长参数格式错误");
        }


        if (vo.getPostType().equals("2") && StringUtils.isEmpty(vo.getId())) {
            return ResultUtil.error("U0995", "更新操作id不允许为空");
        }
        if (vo.getActionFlag().equals("1") && StringUtils.isEmpty(vo.getActionId(), vo.getBlockId(), vo.getTrainingFlag())) {
            return ResultUtil.error("U0995", "是动作，所属动作id 、 所属分组Id 和维护训练动作目标值不可空");
        }
        if (vo.getActionFlag().equals("1") && vo.getTrainingFlag().equals("1")
                && ((StringUtils.isEmpty(vo.getAimDuration()) && StringUtils.isEmpty(vo.getAimTimes()))
                || StringUtils.isEmpty(vo.getEndNodePeriod()))) {
            return ResultUtil.error("U0995", "是训练动作，计数目标时长 和 计数目标次数 不可以同时为空 ,完结动作时长不可以为空");
        }
        //如果开始时间大于或者等于结束时间
        if ((Integer.parseInt(vo.getNodeBeginMinutes()) * 60 + Integer.parseInt(vo.getNodeBeginSeconds()))
                >= Integer.parseInt(vo.getNodeEndMinutes()) * 60 + Integer.parseInt(vo.getNodeEndSeconds())) {
            //入参异常
            return ResultUtil.error("U0995", "开始时间必须小于结束时间");
        }
        if (vo.getCountDownFlag().equals("1") && vo.getTrainingFlag().equals("1")) {
            //入参异常
            return ResultUtil.error("U0995", "维护训练动作目标值时不能勾选需要倒计时");
        }
        return iCourseTrainningNodeInfoService.saveCourseNodeInfo(vo);
    }


    @Log("I288-查询训练课小节信息")
    @ApiOperation(value = "I288-查询训练课小节信息")
    @GetMapping("/getCourseNodeInfo")
    public Result getCourseNodeInfo(String id) {
        if (StringUtils.isEmpty(id)) {
            //入参异常
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
            return iCourseTrainningNodeInfoService.getCourseNodeInfo(id);
        }

    @Log("I289-删除训练课小节信息")
    @ApiOperation(value = "I289-删除训练课小节信息")
    @PostMapping("/deleteCourseNodeInfo")
    public Result deleteCourseNodeInfo(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iCourseTrainningNodeInfoService.deleteCourseNodeInfo(vo.getUserId(), vo.getId());
    }

    //


}

