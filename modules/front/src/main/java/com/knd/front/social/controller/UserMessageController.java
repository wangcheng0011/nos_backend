package com.knd.front.social.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.userutil.UserUtils;
import com.knd.front.social.dto.MessageListDto;
import com.knd.front.social.service.UserMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户消息
 */
@Api(tags = "社交social")
@RestController
@CrossOrigin
@RequestMapping("/front/social")
@RequiredArgsConstructor
public class UserMessageController {
    private final UserMessageService userMessageService;

    @Log("获取用户全部消息列表")
    @ApiOperation(value = "获取用户全部消息列表",notes = "获取用户全部消息列表")
    @GetMapping("/getAllMessageList")
    public Result<MessageListDto> getAllMessageList(@RequestParam(required = false, name = "current") String current,
                                                 @RequestParam(required = false, name = "userId") String userId){
        if(StringUtils.isEmpty(userId)) {
            userId = UserUtils.getUserId();
        }
        if(StringUtils.isEmpty(current)){
            current = "1";
        }
        return userMessageService.getAllMessage(current,userId);
    }

    @Log("获取用户评论列表")
    @ApiOperation(value = "获取用户评论列表",notes = "获取用户评论列表")
    @GetMapping("/getCommentList")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功",response = CommentListDto.class)
//    })
    public Result<MessageListDto> getCommentList(@RequestParam(required = false, name = "current") String current,
                                                 @RequestParam(required = false, name = "userId") String userId){
        if(StringUtils.isEmpty(userId)) {
            userId = UserUtils.getUserId();
        }
        if(StringUtils.isEmpty(current)){
            current = "1";
        }
        return userMessageService.getComment(current,userId);
    }

    @Log("获取用户被@消息列表")
    @ApiOperation(value = "获取用户被@消息列表",notes = "获取用户被@消息列表")
    @GetMapping("/getBeCallList")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功",response = BeCallListDto.class)
//    })
    public Result<MessageListDto> getBeCallList(@RequestParam(required = false, name = "current") String current,
                                               @RequestParam(required = false, name = "userId") String userId){
        if(StringUtils.isEmpty(userId)) {
            userId = UserUtils.getUserId();
        }
        if(StringUtils.isEmpty(current)){
            current = "1";
        }
        return userMessageService.getBeCall(current,userId);
    }

    @Log("获取用户被赞列表")
    @ApiOperation(value = "获取用户被赞列表",notes = "获取用户被赞列表")
    @GetMapping("/getBePraisedList")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功",response = BePraisedListDto.class)
//    })
    public Result<MessageListDto> getBePraisedList(@RequestParam(required = false, name = "current") String current,
                                                     @RequestParam(required = false, name = "userId") String userId){
        if(StringUtils.isEmpty(userId)) {
            userId = UserUtils.getUserId();
        }
        if(StringUtils.isEmpty(current)){
            current = "1";
        }
        return userMessageService.getBePraised(current,userId);
    }

}
