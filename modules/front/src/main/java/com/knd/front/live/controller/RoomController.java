package com.knd.front.live.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.qiniu.RtcRoomManager;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.live.dto.RoomListDto;
import com.knd.front.live.request.CreateOrUpdateRoomRequest;
import com.knd.front.live.request.QueryLiveRoomRequest;
import com.knd.front.live.request.ReportRoomRequest;
import com.knd.front.live.service.IRoomService;
import com.qiniu.http.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author will
 * @date 2021年05月19日 16:03
 */
@RestController
@CrossOrigin
@RequestMapping("/front/live")
@Slf4j
@Api(tags = "房间管理")
public class RoomController {
    @Autowired
    private RtcRoomManager rtcRoomManager;

    @Autowired
    private IRoomService iRoomService;


    @PostMapping("/room/createRoom")
    @Log("创建房间")
    @ApiOperation(value = "创建房间", notes = "创建房间")
    public Result createRoom(@RequestBody @Valid CreateOrUpdateRoomRequest createOrUpdateRoomRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        if ("1".equals(createOrUpdateRoomRequest.getPostType())) {
            return iRoomService.createRoom(createOrUpdateRoomRequest);
        } else {
            if (StringUtils.isEmpty(createOrUpdateRoomRequest.getId())) {
                return ResultUtil.error("U0995", "主键ID不能为空");
            }
            return iRoomService.editRoom(createOrUpdateRoomRequest);
        }

    }

    @PostMapping("/room/deleteRoom")
    @Log("删除房间")
    @ApiOperation(value = "删除房间", notes = "删除房间")
    @Deprecated
    public Result deleteRoom(@ApiParam("主键Id") @RequestParam(required = true, name = "id") String id) {
        return iRoomService.deleteRoom(id);
    }

//    @PostMapping("/trainGroup/changeRoomStatus")
//    @Log("变更房间状态")
//    @ApiOperation(value = "变更房间状态",notes = "变更房间状态")
//    public Result changeRoomStatus(@RequestParam(required = true,name = "主键Id") String id,@ApiParam(example = "0->关闭 1->开始直播") @RequestParam(required = true,name = "状态")String status){
//        if ("0".equals(status)|| "1".equals(status)){
//            return iRoomService.changeRoomStatus(id,status);
//        }
//        return ResultUtil.error("U0995", "参数有误");
//    }

    @PostMapping("/trainGroup/closeRoom")
    @Log("关闭房间")
    @Deprecated
    @ApiOperation(value = "关闭房间 只有房主才能关闭房间", notes = "关闭房间 只有房主才能关闭房间")
    public Result closeRoom(@ApiParam("主键roomId") @RequestParam(required = true, name = "roomId") String id) {

        return iRoomService.closeRoom(id);
    }

    @PostMapping("/trainGroup/closeRoomForManage")
    @Log("训练小组 管理员关闭房间")
    @ApiOperation(value = "训练小组 管理员关闭房间 管理员和创建人关闭房间", notes = "训练小组 管理员关闭房间 管理员和创建人关闭房间")
    public Result closeRoomForManage(@ApiParam("主键Id") @RequestParam(required = true, name = "id") String id) {
        return iRoomService.closeRoomForManage(id);
    }


    @PostMapping("/room/reserve")
    @Log("预约房间")
    @ApiOperation(value = "预约房间", notes = "预约房间")
    public Result reserve(@ApiParam("房间Id") @RequestParam(required = true, name = "id") String id) {
        return iRoomService.reserve(id);
    }

    @GetMapping("/room/roomReserveList")
    @Log("预约房间列表")
    @ApiOperation(value = "预约房间列表", notes = "预约房间列表")
    public Result<Page<RoomListDto>> roomReserveList() {

        return iRoomService.roomReserveList("");
    }


    @GetMapping("/room/roomList")
    @Log("房间列表")
    @ApiOperation(value = "房间列表", notes = "房间列表")
    public Result<Page<RoomListDto>> roomList(@Valid QueryLiveRoomRequest queryLiveRoomRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iRoomService.roomList(queryLiveRoomRequest);
    }

    @GetMapping("/room/getRoomToken")
    public Result getRoomToken(@ApiParam("主键Id") @RequestParam(required = true, name = "roomId") String roomId,
                               @ApiParam("房间验证码") @RequestParam(required = false, name = "invitationCode") String invitationCode) throws Exception {
       /*try {
            String roomToken = rtcRoomManager.getRoomToken("fob0pqv07", id, "111", 1621908664, "admin");
           log.info("获取签发roomToken："+roomToken);
       } catch (Exception e) {
            e.printStackTrace();
        }*/
        return iRoomService.getRoomToken(roomId, invitationCode);
    }

    @GetMapping("/room/listActiveRooms")
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    @Deprecated
    public Result listActiveRooms() {
        try {
            Response resp = rtcRoomManager.listActiveRooms("fob0pqv07", null, 1, 10);
            log.info("获取房间列表：" + resp.getInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.success();
    }

    @GetMapping("/room/listUser")
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表 用不到")
    @Deprecated
    public Result listUser() {
        try {
            Response resp = rtcRoomManager.listUser("fob0pqv07", "test1");
            log.info("获取用户列表：" + resp.getInfo());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.success();
    }

    @PostMapping("/room/reportRoom")
    @Log("举报房间")
    @ApiOperation(value = "举报房间", notes = "举报房间")
    public Result reportRoom(@RequestBody @Valid ReportRoomRequest reportRoomRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iRoomService.reportRoom(reportRoomRequest);
    }
}
