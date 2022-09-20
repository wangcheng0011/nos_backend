package com.knd.front.social.controller;

import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.userutil.UserUtils;
import com.knd.front.social.dto.UserAttachDto;
import com.knd.front.social.dto.UserInfoDto;
import com.knd.front.social.dto.UserMomentDto;
import com.knd.front.social.dto.UserRecordDto;
import com.knd.front.social.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 其他用户个人信息
 */
@Api(tags = "社交social")
@RestController
@CrossOrigin
@RequestMapping("/front/social")
@RequiredArgsConstructor
public class UserInformationController {
    private final UserInfoService userInfoService;

    @Log("获取其他用户详情信息")
    @ApiOperation(value = "获取其他用户详情信息",notes = "获取其他用户详情信息")
    @GetMapping("/getOtherUserMessageList")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功",response = UserInfoDto.class)
//    })
    public Result<UserInfoDto> getOtherUserMessageList(@RequestParam(required = false, name = "userId") String userId,
                                     @RequestParam(required = true, name = "friendId") String friendId){
        if(StringUtils.isEmpty(userId)) {
            userId = UserUtils.getUserId();
        }
        return userInfoService.getOtherInfo(userId,friendId);
    }

    @Log("获取用户记录信息")
    @ApiOperation(value = "获取用户记录信息",notes = "获取用户记录信息")
    @GetMapping("/getRecord")
    public Result<UserRecordDto> getRecord(@RequestParam(required = true, name = "friendId") String friendId){
        return userInfoService.getRecord(friendId);
    }

    @Log("获取用户相册信息")
    @ApiOperation(value = "获取用户相册信息",notes = "获取用户相册信息")
    @GetMapping("/getAlbum")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功",response = UserAttachDto.class)
//    })
    public Result<UserAttachDto> getAlbum(@RequestParam(required = true, name = "friendId") String friendId){
        return userInfoService.getAlbum(friendId);
    }

    @Log("获取用户动态信息")
    @ApiOperation(value = "获取用户动态信息",notes = "获取用户动态信息")
    @GetMapping("/getMoment")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功",response = UserMomentDto.class)
//    })
    public Result<UserMomentDto> getMoment(@RequestParam(required = true, name = "current") String current,
                                           @RequestParam(required = true, name = "userId") String userId,
                                            @RequestParam(required = true, name = "friendId") String friendId){
        return userInfoService.getMoment(current,userId,friendId);
    }

    @Log("获取用户单条动态信息")
    @ApiOperation(value = "获取用户单条动态信息",notes = "获取用户单条动态信息")
    @GetMapping("/getOneMoment")
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "成功",response = UserMomentDto.class)
//    })
    public Result<UserMomentDto> getOneMoment(@RequestParam(required = true, name = "userId") String userId,
                                              @RequestParam(required = true, name = "momentId") String momentId){
        return userInfoService.getOneMoment(userId,momentId);
    }



}
