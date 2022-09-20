package com.knd.manage.user.controller;


import com.knd.common.basic.StringUtils;
import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.common.response.ResultEnum;
import com.knd.common.response.ResultUtil;
import com.knd.common.userutil.UserUtils;
import com.knd.manage.user.service.IUserService;
import com.knd.manage.user.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

@Api(tags = "云端管理-user")
@RestController
@CrossOrigin
@Log4j2
@RequestMapping("/admin/user")
public class UserController {
    @Resource
    private IUserService iUserService;


    @Log("I310-查询注册会员列表")
    @ApiOperation(value = "I310-查询注册会员列表")
    @GetMapping("/queryRegistedUserList")
    public Result queryRegistedUserList(String nickName, String mobile, String frozenFlag, String registTimeBegin,
                                        String registTimeEnd, String current) throws ParseException {
        //数据检查
        if (StringUtils.isEmpty(current)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iUserService.queryRegistedUserList(nickName, mobile, frozenFlag, registTimeBegin, registTimeEnd, current);
    }

    @Log("I312-查询注册会员训练列表")
    @ApiOperation(value = "I312-查询注册会员训练列表")
    @GetMapping("/queryUserTrainList")
    public Result queryUserTrainList(String nickName, String mobile, String equipmentNo, String trainTimeBegin,
                                     String trainTimeEnd, String current, String trainType) throws ParseException {
        //数据检查
        if (StringUtils.isEmpty(current)) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iUserService.queryUserTrainList(nickName, mobile, equipmentNo, trainTimeBegin, trainTimeEnd, current, trainType);
    }


    @Log("I313-查询注册会员训练详情")
    @ApiOperation(value = "I313-查询注册会员训练详情")
    @GetMapping("/queryUserTrainInfo")
    public Result queryUserTrainInfo(@Validated VoQueryUserTrainInfo vo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iUserService.queryUserTrainInfo(vo);
    }


    @Log("I317-保存注册会员状态")
    @ApiOperation(value = "I317-保存注册会员状态")
    @PostMapping("/saveFrozenFlag")
    public Result saveFrozenFlag(@RequestBody @Validated VoSaveFrozenFlag vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iUserService.saveFrozenFlag(vo.getUserId(), vo);
    }

    @Log("I318-删除注册会员")
    @ApiOperation(value = "I318-删除注册会员")
    @PostMapping("/deleteUser")
    public Result deleteUser(@RequestBody @Validated VoDeleteUser vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iUserService.deleteUser(vo);
    }

    @Log("I318-更新用户微信信息")
    @ApiOperation(value = "I318-更新用户微信信息")
    @PostMapping("/updateUserWxInfo")
    public Result updateUserWxInfo(@RequestBody @Validated VoSaveUserWxInfo vo, BindingResult bindingResult) {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        //校验参数
        if (bindingResult.hasErrors()) {
            //参数校验失败
            return ResultUtil.error(ResultEnum.PARAM_ERROR);
        }
        return iUserService.updateUserWxInfo(vo);
    }

    @Log("I318-保存微信头像")
    @ApiOperation(value = "I318-保存微信头像")
    @PostMapping("/uploadWxPicture")
    public Result uploadWxPicture(HttpServletRequest request, HttpServletResponse response,VoSaveUserWxPicInfo vo) throws Exception {
        if(StringUtils.isEmpty(vo.getUserId())){
            //userId从token获取
            vo.setUserId(UserUtils.getUserId());
        }
        return iUserService.uploadWxPicture(vo,request,response);
    }



}

