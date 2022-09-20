package com.knd.front.train.controller;


import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.userutil.UserUtils;
import com.knd.front.home.service.IBaseCourseTypeService;
import com.knd.front.train.request.FilterCourseListRequest;
import com.knd.front.train.request.FilterFreeTrainListRequest;
import com.knd.front.train.service.IBaseBodyPartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author llx
 * @since 2020-07-02
 */
@RestController
@CrossOrigin
@RequestMapping("/front/train")
@Slf4j
@Api(tags = "train")
public class BaseBodyPartController {
    @Autowired
    private IBaseBodyPartService iBaseBodyPartService;
    @Autowired
    private IBaseCourseTypeService iBaseCourseTypeService;
    @PostMapping("/getFilterCourseList")
    @Log("I061-获取筛序课程分类列表")
    @ApiOperation(value = "I061-获取筛序课程分类列表", notes = "I061-获取筛序课程分类列表")
    public Result getFilterCourseList(@RequestBody(required = false) FilterCourseListRequest filterCourseListRequest) {
        String userId = UserUtils.getUserId();
        filterCourseListRequest.setUserId(userId);
        return iBaseBodyPartService.getFilterCourseList(filterCourseListRequest);
    }


    @PostMapping("/getFilterFreeTrainList")
    @Log("I081-获取动作分类列表")
    @ApiOperation(value = "I081-获取动作分类列表",notes = "I081-获取动作分类列表")
    public Result getFilterFreeTrainList(@RequestBody(required = false) FilterFreeTrainListRequest filterFreeTrainListRequest){
        return iBaseBodyPartService.getFilterFreeTrainList(filterFreeTrainListRequest);
    }



    @GetMapping("/getFilterCourseLabelSettings")
    @Log("I060-获取课程筛序分类标签")
    @ApiOperation(value = "I060-获取课程筛序分类标签", notes = "I060-获取课程筛序分类标签")
    public Result getFilterCourseLabelSettings(@RequestParam(required = false) String userId) {
        return iBaseCourseTypeService.getFilterCourseLabelSettings(userId);
    }
}

