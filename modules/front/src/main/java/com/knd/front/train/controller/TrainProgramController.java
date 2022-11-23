package com.knd.front.train.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.front.train.request.GetTrainProgramRequest;
import com.knd.front.train.request.SaveTrainProgramRequest;
import com.knd.front.train.request.VoId;
import com.knd.front.train.service.ProgramService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.ParseException;

/**
 * @author will
 */
@RestController
@CrossOrigin
@RequestMapping("/front/train")
@Slf4j
@Api(tags = "trainProgram")
public class TrainProgramController {

    @Autowired
    private ProgramService  programService;

    @Log("I11X-保存训练计划")
    @ApiOperation(value = "I11X-保存训练计划",notes = "I11X-保存训练计划")
    @PostMapping("/saveTrainProgram")
    @ResponseBody
    public Result saveTrainProgram(@RequestBody @Valid SaveTrainProgramRequest saveTrainProgramRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return programService.saveTrainProgram(saveTrainProgramRequest);
    }

    @Log("I11X-获取用户训练计划")
    @ApiOperation(value = "I11X-获取用户训练计划",notes = "I11X-获取用户训练计划")
    @GetMapping("/getTrainProgram")
    public Result getTrainProgram(@Valid GetTrainProgramRequest getTrainProgramRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return programService.getTrainProgram(getTrainProgramRequest);
    }

    @Log("I11X-训练计划请假")
    @ApiOperation(value = "I11X-训练计划请假",notes = "I11X-训练计划请假")
    @GetMapping("/takeRestTrainProgram")
    public Result takeRestTrainProgram(@RequestParam @NotNull String trainProgramId,@RequestParam @NotNull String userId,@RequestParam @NotNull String restDate) throws ParseException {

        return programService.takeRestTrainProgram(trainProgramId,userId,restDate);
    }

    @Log("I11X-删除训练计划")
    @ApiOperation(value = "I11X-删除训练计划",notes = "I11X-删除训练计划")
    @PostMapping("/deleteTrainProgram")
    public Result deleteTrainProgram(@RequestBody @Validated VoId vo, BindingResult bindingResult){
        //userId从token获取
        //vo.setUserId(UserUtils.getUserId());
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return programService.deleteTrainProgram(vo.getUserId(),vo.getId());
    }

    @Log("I11X-查询历史训练计划")
    @ApiOperation(value = "I11X-查询历史训练计划",notes = "I11X-查询历史训练计划 结束时间在当前时间之前，传userId")
    @GetMapping("/queryHistoryTrainProgram")
    public Result queryHistoryTrainProgram(String current){
        //userId从token获取
         String userId = UserUtils.getUserId();
        // String userId = "dM2ejezT";
        log.info("queryHistoryTrainProgram userId:{{}}",userId);
        return programService.queryHistoryTrainProgram(userId,current);
    }

    @Log("I045-训练计划推送")
    @ApiOperation(value = "I045-训练计划推送", notes = "I045-训练计划推送")
    @PostMapping("/trainProgramPush")
    public Result trainProgramPush() {
        return ResultUtil.success(programService.trainProgramPush());
    }

}