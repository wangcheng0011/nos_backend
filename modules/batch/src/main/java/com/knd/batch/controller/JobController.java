package com.knd.batch.controller;

import com.knd.batch.entity.JobEntry;
import com.knd.batch.entity.JobRun;
import com.knd.batch.entity.JobSearch;
import com.knd.batch.service.SchedulerService;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Slf4j
@Api(tags = "批处理控制器")
@RestController
@CrossOrigin
@RequestMapping("job")
public class JobController {

    @Resource
    private SchedulerService schedulerService;

    @ApiOperation("任务添加")
    @PostMapping(value = "/addJob")
    public Result addJob(@Valid JobEntry jobEntry) throws InstantiationException, SchedulerException, IllegalAccessException {
        schedulerService.addJob(jobEntry);
        return ResultUtil.success();
    }

    @ApiOperation("任务更新")
    @PutMapping(value = "/updateJob")
    public Result updateJob(@Valid JobEntry jobEntry) throws SchedulerException {
        schedulerService.updateJob(jobEntry);
        return ResultUtil.success();
    }

    @ApiOperation("删除任务")
    @PutMapping(value = "/deleteJob")
    public Result deleteJob(@Valid JobSearch jobSearch) throws SchedulerException {
        schedulerService.deleteJob(jobSearch);
        return ResultUtil.success();
    }

    @ApiOperation("获取任务列表")
    @GetMapping(value = "/jobList")
    public Result jobList() throws SchedulerException {
        return ResultUtil.success(schedulerService.queryAllJob());
    }

    @ApiOperation("立即执行任务")
    @PostMapping(value = "/runAJobNow")
    public Result runAJobNow(@Valid JobRun run) throws SchedulerException {
        schedulerService.runAJobNow(run);
        return ResultUtil.success();
    }
//    //用于微服务调用的测试
//    @ApiOperation("立即执行任务")
//    @PostMapping(value = "/runAJobNow")
//    public Result runAJobNow2() throws SchedulerException {
////        System.out.println("立即执行任务");
//        JobRun run = new JobRun();
//        run.setId(1);
//        run.setJobClassName("com.knd.batch.job.DoLevelJob");
//        run.setJobGroupName("group1");
////        System.out.println(run.toString());
//        schedulerService.runAJobNow(run);
//        return ResultUtil.success("触发成功，这里是异步操作，具体运行信息需要查看服务器上的日志");
//    }

    @ApiOperation("暂停任务")
    @PutMapping(value = "/pauseJob")
    public Result pauseJob(@Valid JobSearch jobSearch) throws SchedulerException {
        schedulerService.pauseJob(jobSearch);
        return ResultUtil.success();
    }

    @ApiOperation("恢复任务")
    @PutMapping(value = "/resumeJob")
    public Result resumeJob(@Valid JobSearch jobSearch) throws SchedulerException {
        schedulerService.resumeJob(jobSearch);
        return ResultUtil.success();
    }


}
