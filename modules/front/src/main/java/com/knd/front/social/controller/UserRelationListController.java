package com.knd.front.social.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.userutil.UserUtils;
import com.knd.front.social.dto.UserFansDto;
import com.knd.front.social.dto.UserFollowDto;
import com.knd.front.social.dto.UserFriendsDto;
import com.knd.front.social.service.UserFriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户交际关系
 */
@Api(tags = "社交social")
@RestController
@CrossOrigin
@RequestMapping("/front/social")
@RequiredArgsConstructor
public class UserRelationListController {
    private final UserFriendService userFriendService;

    @Log("获取用户好友列表信息")
    @ApiOperation(value = "获取用户好友列表信息",notes = "获取用户好友列表信息")
    @GetMapping("/getFriendList")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功",response = UserFriendsDto.class)
//    })
    public Result<UserFriendsDto> getFriendList(@RequestParam(required = false, name = "current") String current,
                                                @RequestParam(required = false, name = "userId") String userId){
        if(StringUtils.isEmpty(userId)) {
            userId = UserUtils.getUserId();
        }
        if (StringUtils.isEmpty(current)){
            current = "1";
        }
        return userFriendService.getFriendsList(current,userId);
    }

    @Log("获取用户粉丝列表信息")
    @ApiOperation(value = "获取用户粉丝列表信息",notes = "获取用户粉丝列表信息")
    @GetMapping("/getFanList")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功",response = UserFansDto.class)
//    })
    public Result<UserFansDto> getFanList(@RequestParam(required = false, name = "current") String current,
                                          @RequestParam(required = false, name = "userId") String userId){
        if(StringUtils.isEmpty(userId)) {
            userId = UserUtils.getUserId();
        }
        if (StringUtils.isEmpty(current)){
            current = "1";
        }
        return userFriendService.getFanList(current,userId);
    }

    @Log("获取用户关注列表信息")
    @ApiOperation(value = "获取用户关注列表信息",notes = "获取用户关注列表信息")
    @GetMapping("/getFollowList")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功",response = UserFollowDto.class)
//    })
    public Result<UserFollowDto> getFollowList(@RequestParam(required = false, name = "current") String current,
                                               @RequestParam(required = false, name = "userId") String userId){
        if(StringUtils.isEmpty(userId)) {
            userId = UserUtils.getUserId();
        }
        if (StringUtils.isEmpty(current)){
            current = "1";
        }
        return userFriendService.getFollowList(current,userId);
    }
}
