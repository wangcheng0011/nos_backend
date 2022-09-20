package com.knd.front.live.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.front.live.dto.UserRecordListDto;
import com.knd.front.live.service.UserOrderRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author zm
 */
@RestController
@CrossOrigin
@RequestMapping("/front/recordMessage")
@Slf4j
@Api(tags = "消息中心")
@RequiredArgsConstructor
public class MessageController {

    private final UserOrderRecordService userOrderRecordService;

    @Log("获取用户消息列表")
    @ApiOperation(value = "获取用户消息列表",notes = "获取用户消息列表")
    @GetMapping("/getMessageList")
    public Result<Page<UserRecordListDto>> getMessageList(@ApiParam("用户id") @RequestParam(required = true) String userId,
                                                          @ApiParam("当前页") @RequestParam(required = true) String current){
        return userOrderRecordService.getRecordList(userId,current);
    }

    @Log("读取消息")
    @ApiOperation(value = "读取消息",notes = "读取消息")
    @PostMapping("/read")
    public Result read(@ApiParam("id") @RequestParam(required = true) String id,
                       @ApiParam("用户id") @RequestParam(required = true) String userId) {
        return userOrderRecordService.read(id,userId);
    }

}
