package com.knd.front.live.controller;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.front.live.service.ILiveCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author will
 * @date 2021年07月05日 11:42
 */
@RestController
@CrossOrigin
@RequestMapping("/front/live")
@Slf4j
@Api(tags = "直播课程管理")
public class LiveCourseController {

    @Autowired
    private ILiveCourseService iLiveCourseService;



    @GetMapping("/course/getRoomToken")
    @Log("获取roomToken")
    @ApiOperation(value = "获取直播课程roomToken",notes = "获取直播课程roomToken")
    public Result getRoomToken(@ApiParam("方式:1观看,2直播")@RequestParam(required = true,name = "type") String type,
                               @ApiParam("预约课程时段Id或courseId")@RequestParam(required = true,name = "timeId") String timeId){

        return iLiveCourseService.getRoomToken(timeId,type);
    }

    @PostMapping("/course/endCourse")
    @Log("结束直播课")
    @ApiOperation(value = "结束直播课",notes = "结束直播课")
    public Result endCourse(@ApiParam("课程时段Id")@RequestParam(required = true,name = "id") String id){
        return iLiveCourseService.endCourse(id);
    }

    @PostMapping("/course/closeUserCoachTime")
    @Log("私教关闭直播")
    @ApiOperation(value = "私教关闭直播", notes = "私教关闭直播")
    public Result closeUserCoachTime(@ApiParam("主键Id") @RequestParam(required = true, name = "id") String id) {
        return iLiveCourseService.closeUserCoachTime(id);
    }

}
