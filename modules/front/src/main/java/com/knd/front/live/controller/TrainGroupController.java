package com.knd.front.live.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.domain.RankingTypeEnum;
import com.knd.front.live.dto.ApplyListDto;
import com.knd.front.live.dto.GroupUserListDto;
import com.knd.front.live.dto.TrainGroupDto;
import com.knd.front.live.dto.TrainGroupListDto;
import com.knd.front.live.request.CreateOrUpdateTrainGroupRequest;
import com.knd.front.live.request.QueryTrainGroupRequest;
import com.knd.front.live.service.ITrainGroupService;
import com.knd.front.user.dto.RankingListDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

/**
 * @author will
 * @date 2021年05月19日 16:03
 */
@RestController
@CrossOrigin
@RequestMapping("/front/live")
@Slf4j
@Api(tags = "训练小组管理")
public class TrainGroupController {


    @Autowired
    private ITrainGroupService iTrainGroupService;



    @PostMapping("/trainGroup/createGroup")
    @Log("创建小组")
    @ApiOperation(value = "创建小组",notes = "创建小组")
    public Result createGroup(@RequestBody @Valid CreateOrUpdateTrainGroupRequest createOrUpdateTrainGroupRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        if("1".equals(createOrUpdateTrainGroupRequest.getPostType())) {
            return iTrainGroupService.createGroup(createOrUpdateTrainGroupRequest);
        }else{
            if (StringUtils.isEmpty(createOrUpdateTrainGroupRequest.getId())){
                return ResultUtil.error("U0995", "主键ID不能为空");
            }
            return iTrainGroupService.editGroup(createOrUpdateTrainGroupRequest);
        }

    }

    @PostMapping("/trainGroup/deleteRoom")
    @Log("删除小组")
    @ApiOperation(value = "删除小组",notes = "删除小组 用不到")
    public Result deleteRoom(@ApiParam("小组Id")@RequestParam(required = true,name = "id") String id){
        return iTrainGroupService.deleteGroup(id);
    }

    @PostMapping("/trainGroup/joinGroup")
    @Log("申请加入小组")
    @ApiOperation(value = "申请加入小组",notes = "申请加入小组")
    public Result joinGroup(@ApiParam("小组Id") @RequestParam(required = true,name = "groupId") String groupId){
        return iTrainGroupService.joinGroup(groupId);
    }

    @GetMapping("/trainGroup/applyList")
    @Log("申请加入列表")
    @ApiOperation(value = "申请加入列表",notes = "申请加入列表")
    public Result<Page<ApplyListDto>> applyList(
            @ApiParam("小组Id")@RequestParam(required = true,name = "groupId") String groupId,
            @ApiParam("申请状态")@RequestParam(required = false,name = "applyStatus") String applyStatus,
            @ApiParam("当前请求页")@RequestParam(required = true,name = "currentPage") String currentPage){
        if (StringUtils.isEmpty(currentPage)){
            return ResultUtil.error("U0995", "当前请求页不能为空");
        }
        return iTrainGroupService.applyList(groupId,applyStatus,currentPage);
    }
    @GetMapping("/trainGroup/getGroupRole")
    @Log("获取小组身份")
    @ApiOperation(value = "获取小组身份",notes = "获取小组身份 -1->创建人 0-》普通成员 1-》管理员 ")
    public Result<String> getGroupRole(
            @ApiParam("小组Id")@RequestParam(required = true,name = "groupId") String groupId){

        return iTrainGroupService.getGroupRole(groupId);
    }

    @GetMapping("/trainGroup/groupUserList")
    @Log("小组成员列表分页")
    @ApiOperation(value = "小组成员列表分页",notes = "小组成员列表分页")
    public Result<Page<GroupUserListDto>> groupUserList(
            @ApiParam("小组Id")@RequestParam(required = true,name = "groupId") String groupId,
            @ApiParam("当前请求页") @RequestParam(required = true,name = "currentPage") String currentPage){
        if (StringUtils.isEmpty(currentPage)){
            return ResultUtil.error("U0995", "当前请求页不能为空");
        }
        return iTrainGroupService.groupUserList(groupId,currentPage);
    }

    @GetMapping("/trainGroup/groupUserListNew")
    @Log("小组成员列表非分页")
    @ApiOperation(value = "小组成员列表非分页",notes = "小组成员列表非分页")
    public Result<GroupUserListDto> groupUserListNew(
            @ApiParam("小组Id")@RequestParam(required = true,name = "groupId") String groupId){
        return iTrainGroupService.groupUserList(groupId);
    }

    @PostMapping("/trainGroup/signOutGroup")
    @Log("退出小组")
    @ApiOperation(value = "退出小组",notes = "退出小组")
    public Result signOutGroup(@ApiParam("小组Id")@RequestParam(required = true,name = "groupId") String groupId){
        return iTrainGroupService.signOutGroup(groupId);
    }


    @PostMapping("/trainGroup/kickOutGroup")
    @Log("移出小组")
    @ApiOperation(value = "移出小组",notes = "移出小组")
    public Result kickOutGroup(
            @ApiParam("小组Id")@RequestParam(required = true,name = "groupId") String groupId){
        return iTrainGroupService.kickOutGroup(groupId);
    }

    @PostMapping("/trainGroup/kickOutGroupForManage")
    @Log("管理员移出小组")
    @ApiOperation(value = "管理员移出小组",notes = "管理员移出小组")
    public Result kickOutGroupForManage(
            @ApiParam("小组Id")@RequestParam(required = true,name = "groupId") String groupId){
        return iTrainGroupService.kickOutGroupForManage(groupId);
    }


    @PostMapping("/trainGroup/examine")
    @Log("审批申请")
    @ApiOperation(value = "审批申请",notes = "审批申请")
    public Result examine(@ApiParam("主键ID")@RequestParam(required = true,name = "id") String id
            ,@ApiParam("审核状态")@RequestParam(required = true,name = "status") String status){

        if("1".equals(status) ||"2".equals(status)) {
            return iTrainGroupService.examine(id,status);
        }else{
            return ResultUtil.error("U0995", "审核状态只能是1-》通过 | 2-》拒绝");
        }

    }

    @PostMapping("/trainGroup/changeUserRole")
    @Log("变更成员角色")
    @ApiOperation(value = "变更成员角色",notes = "变更成员角色")
    public Result changeUserRole(@ApiParam("主键ID")@RequestParam(required = true,name = "id") String id
            ,@ApiParam("角色 0-》普通成员 1-》管理员")@RequestParam(required = true,name = "isAdmin") String isAdmin) throws Exception {

        if("0".equals(isAdmin) ||"1".equals(isAdmin)) {
            return iTrainGroupService.changeUserRole(id,isAdmin);
        }else{
            return ResultUtil.error("U0995", "审核状态只能是0-》普通成员 | 1-》管理员");
        }
    }


    @GetMapping("/trainGroup/groupList")
    @Log("小组列表")
    @ApiOperation(value = "小组列表",notes = "小组列表")
    public Result<Page<TrainGroupListDto>> groupList(@Valid QueryTrainGroupRequest queryTrainGroupRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            String error = bindingResult.getFieldError().getDefaultMessage();
            return ResultUtil.error("U0995", error);
        }
        return iTrainGroupService.groupList(queryTrainGroupRequest);
    }

    @GetMapping("/trainGroup/groupById")
    @Log("根据id获取小组详情")
    @ApiOperation(value = "根据id获取小组详情",notes = "根据id获取小组详情")
    public Result<TrainGroupDto> groupById(@RequestParam(required = true) String groupId){

        return iTrainGroupService.groupById(groupId);
    }


    @Log("获取小组爆发力,总力量,毅力排行榜")
    @ApiOperation(value = "获取小组爆发力,总力量,毅力排行榜",notes = "获取小组爆发力,总力量,毅力排行榜")
    @GetMapping("/getGroupRankingList")
    public Result<RankingListDto> getGroupRankingList(@RequestParam(required = true) String userId,
                                                      @RequestParam(required = true) String groupId,
                                                      @RequestParam RankingTypeEnum type,
                                                      @RequestParam(required = true) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date){
        return iTrainGroupService.getRankingList(type,date,userId,groupId);

    }
}
