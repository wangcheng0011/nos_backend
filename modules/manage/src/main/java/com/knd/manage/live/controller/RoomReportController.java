package com.knd.manage.live.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.common.vo.VoId;
import com.knd.manage.live.request.ManageRequest;
import com.knd.manage.live.request.RoomReportListRequest;
import com.knd.manage.live.service.RoomReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 举报管理
 */
@Api(tags = "直播房间管理-room")
@RestController
@CrossOrigin
@RequestMapping("/admin/room")
@RequiredArgsConstructor
public class RoomReportController {
    private final RoomReportService roomReportService;

    @Log("获取房间举报列表")
    @ApiOperation(value = "获取房间举报列表")
    @GetMapping("/getRoomReportList")
    public Result getRoomReportList(@Validated RoomReportListRequest vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return roomReportService.getRoomReportList(vo);
    }


    @Log("获取房间举报详情")
    @ApiOperation(value = "获取房间举报详情")
    @GetMapping("/getRoomReport")
    public Result getRoomReport(String id) {
        //数据检查
        if (StringUtils.isEmpty(id) || id.length() > 64) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return roomReportService.getRoomReport(id);
    }


    @Log("举报管理")
    @ApiOperation(value = "举报管理")
    @PostMapping("/operationReport")
    public Result operationReport(@RequestBody @Validated ManageRequest vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //参数校验
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return roomReportService.operation(vo);

    }

    @Log("I211-删除举报")
    @ApiOperation(value = "I211-删除举报")
    @PostMapping("/deleteRoomReport")
    public Result deleteRoomReport(@RequestBody @Validated VoId vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return roomReportService.delete(vo);
    }

    @PostMapping("/trainGroup/closeRoom")
    @Log("关闭私教")
    @ApiOperation(value = "关闭房间",notes = "关闭房间")
    public Result closeRoom(@RequestBody @Validated VoId vo, BindingResult bindingResult){
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return roomReportService.closeRoom(vo.getId());
    }

    @PostMapping("/trainGroup/closeUserCoachTime")
    @Log("关闭直播")
    @ApiOperation(value = "关闭直播",notes = "关闭直播")
    public Result closeUserCoachTime(@RequestBody @Validated VoId vo, BindingResult bindingResult){
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return roomReportService.closeUserCoachTime(vo.getId());
    }

    @PostMapping("/trainGroup/closeUserCoachCourseOrder")
    @Log("关闭私教")
    @ApiOperation(value = "关闭私教",notes = "关闭私教")
    public Result closeUserCoachCourseOrder(@RequestBody @Validated VoId vo, BindingResult bindingResult){
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return roomReportService.closeUserCoachCourseOrder(vo.getId());
    }
}
