package com.knd.front.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.basic.StringUtils;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.entity.*;
import com.knd.front.login.mapper.UserDetailMapper;
import com.knd.front.train.mapper.ActionTypeMapper;
import com.knd.front.train.mapper.BasePowerStandardUseMapper;
import com.knd.front.user.dto.UserPowerTestDto;
import com.knd.front.user.dto.UserTestActionDto;
import com.knd.front.user.mapper.BaseActionMapper;
import com.knd.front.user.mapper.UserActionPowerTestMapper;
import com.knd.front.user.request.UserActionPowerTestRequest;
import com.knd.front.user.service.IUserActionPowerTestService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author will
 */
@Service
public class UserActionPowerTestServiceImpl extends ServiceImpl<UserActionPowerTestMapper, UserActionPowerTest> implements IUserActionPowerTestService {
    @Autowired
    private UserDetailMapper userDetailMapper;

    @Autowired
    private UserActionPowerTestMapper userActionPowerTestMapper;

    @Autowired
    private BasePowerStandardUseMapper basePowerStandardUseMapper;

    @Autowired
    private BaseActionMapper baseActionMapper;

    @Autowired
    private ActionTypeMapper actionTypeMapper;

    @Value("${upload.FileImagesPath}")
    private String fileImagesPath;
    @Value("${upload.FileVideoPath}")
    private String fileVideoPath;

    @Override
    public String getUserTrainPower(String userId,String actionTypeId) {
        float percentage = 1;
        QueryWrapper<UserDetail> userDetailQueryWrapper = new QueryWrapper<>();
        userDetailQueryWrapper.eq("userId",userId);
        userDetailQueryWrapper.eq("deleted", "0");
        UserDetail userDetail = userDetailMapper.selectOne(userDetailQueryWrapper);
        if(userDetail != null) {
            if("增肌".equals(userDetail.getTarget())) {
                percentage = 0.8f;
            }else if("塑形".equals(userDetail.getTarget())) {
                percentage = 0.7f;
            }else{
                percentage = 0.6f;
            }
        }
        //首先检查是否有相关动作类型力量测试，有则直接返回测试力量
        QueryWrapper<UserActionPowerTest> userActionPowerTestQueryWrapper = new QueryWrapper<>();
        userActionPowerTestQueryWrapper.eq("userId",userId);
        userActionPowerTestQueryWrapper.eq("actionType",actionTypeId);
        userActionPowerTestQueryWrapper.eq("deleted","0");
        userActionPowerTestQueryWrapper.last("limit 1");
        UserActionPowerTest userActionPowerTest = userActionPowerTestMapper.selectOne(userActionPowerTestQueryWrapper);
        if(userActionPowerTest != null && !StringUtils.isEmpty(userActionPowerTest.getTestPower())) {
            return (Math.round(Integer.parseInt(userActionPowerTest.getTestPower())*percentage))+"";
        }
        //没有经过相关动作类型测试的，根据用户性别，体重，在基础力量表中适配标准力量
        BasePowerStandardUse basePowerStandardUse = getBasePowerStandarUse(userDetail, actionTypeId);
//        QueryWrapper<UserDetail> userDetailQueryWrapper = new QueryWrapper<>();
//        userDetailQueryWrapper.eq("userId",userId);
//        UserDetail userDetail = userDetailMapper.selectOne(userDetailQueryWrapper);
//        if(userDetail == null) {
//            return "-1";
//        }
//        String weight = userDetail.getWeight();
//        String gender = userDetail.getGender();
//        if(StringUtils.isEmpty(weight) ) {
//            return "-1";
//        }
//        if(StringUtils.isEmpty(gender)) {
//            return "-1";
//        }
//        Integer iWeight = Integer.parseInt(weight);
//        if(iWeight< 60) {
//            if("1".equals(gender)) {
//                iWeight = 60;
//            }else{
//                iWeight = 40;
//            }
//        }
//        if(iWeight> 140) {
//            if("1".equals(gender)) {
//                iWeight = 140;
//            }else{
//                iWeight = 120;
//            }
//        }
//        iWeight =(int)(5 * Math.ceil(iWeight/5.0));
//        QueryWrapper<BasePowerStandardUse> basePowerStandardUseQueryWrapper = new QueryWrapper<>();
//        basePowerStandardUseQueryWrapper.eq("actionType",actionTypeId);
//        basePowerStandardUseQueryWrapper.eq("gender",gender);
//        basePowerStandardUseQueryWrapper.eq("weight",iWeight);
//        BasePowerStandardUse basePowerStandardUse = basePowerStandardUseMapper.selectOne(basePowerStandardUseQueryWrapper);
        if(basePowerStandardUse == null) {
            return "-1";
        }
        return Math.round(Integer.parseInt(basePowerStandardUse.getElementary())*percentage)+"";
    }

    private BasePowerStandardUse getBasePowerStandarUse(UserDetail userDetail,String actionTypeId) {
        String weight = "";
        String gender = "";
        if(userDetail == null) {
            weight = "40";
            gender = "2";
        }else{
            weight = userDetail.getWeight();
            gender = userDetail.getGender();
        }
        if(StringUtils.isEmpty(gender)) {
            gender = "2";
        }
        if(StringUtils.isEmpty(weight) ) {
            if("1".equals(gender)) {
                weight = "60";
            }else{
                weight = "40";
            }
        }

        Integer iWeight = Integer.parseInt(weight);
        if(iWeight< 60) {
            if("1".equals(gender)) {
                iWeight = 60;
            }else{
                iWeight = 40;
            }
        }
        if(iWeight> 140) {
            if("1".equals(gender)) {
                iWeight = 140;
            }else{
                iWeight = 120;
            }
        }
        iWeight =(int)(5 * Math.ceil(iWeight/5.0));
        QueryWrapper<BasePowerStandardUse> basePowerStandardUseQueryWrapper = new QueryWrapper<>();
        basePowerStandardUseQueryWrapper.eq("actionType",actionTypeId);
        basePowerStandardUseQueryWrapper.eq("gender",gender);
        basePowerStandardUseQueryWrapper.eq("weight",iWeight);
        basePowerStandardUseQueryWrapper.eq("deleted", "0");
        BasePowerStandardUse basePowerStandardUse = basePowerStandardUseMapper.selectOne(basePowerStandardUseQueryWrapper);
        if(basePowerStandardUse == null) {
            return null;
        }
        return basePowerStandardUse;
    }

    @Override
    @Transactional
    public Result saveUserTranPower(List<UserActionPowerTestRequest> userActionPowerTestRequest) {
        userActionPowerTestRequest.forEach(item ->{
            UserActionPowerTest userActionPowerTest = new UserActionPowerTest();
            BeanUtils.copyProperties(item,userActionPowerTest);
            //userActionPowerTestMapper.insert(userActionPowerTest);

            QueryWrapper<UserActionPowerTest> userActionPowerTestQueryWrapper = new QueryWrapper<>();
            userActionPowerTestQueryWrapper.eq("userId",item.getUserId());
            userActionPowerTestQueryWrapper.eq("actionType",item.getActionType());
            userActionPowerTestQueryWrapper.eq("deleted","0");
            UserActionPowerTest userActionPowerTest1 = userActionPowerTestMapper.selectOne(userActionPowerTestQueryWrapper);
            if(userActionPowerTest1 != null) {
                userActionPowerTest.setId(userActionPowerTest1.getId());
                userActionPowerTest.setDeleted("1");
                userActionPowerTestMapper.update(userActionPowerTest,userActionPowerTestQueryWrapper);
            }
            userActionPowerTestMapper.insert(userActionPowerTest);
        });
        return ResultUtil.success();
    }

    @Override
    public Result getActionPowerTestResult(String userId) {
        QueryWrapper<UserActionPowerTest> userActionPowerTestQueryWrapper = new QueryWrapper<>();
        userActionPowerTestQueryWrapper.eq("userId",userId);
        userActionPowerTestQueryWrapper.eq("deleted","0");
        userActionPowerTestQueryWrapper.orderByAsc("length(actionType)","actionType");
        List<UserActionPowerTest> userActionPowerTest = userActionPowerTestMapper.selectList(userActionPowerTestQueryWrapper);
        return ResultUtil.success(userActionPowerTest);
    }

    @Override
    public Result getActionPowerTestList(String userId) {
        List<UserPowerTestDto> userPowerTestDtos = new ArrayList<>();
        QueryWrapper<UserDetail> userDetailQueryWrapper = new QueryWrapper<>();
        userDetailQueryWrapper.eq("userId",userId);
        userDetailQueryWrapper.eq("deleted", "0");
        UserDetail userDetail = userDetailMapper.selectOne(userDetailQueryWrapper);
        QueryWrapper<ActionType> actionTypeQueryWrapper = new QueryWrapper<>();
        actionTypeQueryWrapper.ne("id","0");
        actionTypeQueryWrapper.eq("deleted","0");
        actionTypeQueryWrapper.orderByAsc("length(id)","id");
        actionTypeQueryWrapper.select("id","name");
        List<ActionType> actionTypes = actionTypeMapper.selectList(actionTypeQueryWrapper);
        actionTypes.forEach(e ->{
            UserPowerTestDto userPowerTestDto = new UserPowerTestDto();
            BasePowerStandardUse basePowerStandarUse = getBasePowerStandarUse(userDetail, e.getId());
            userPowerTestDto.setBasePowerStandardUse(basePowerStandarUse);
//            QueryWrapper<BaseAction> baseActionQueryWrapper = new QueryWrapper<>();
//            baseActionQueryWrapper.eq("actionType",e.getId());
//            baseActionQueryWrapper.eq("strengthTestFlag","1");
//            baseActionQueryWrapper.eq("deleted","0");
            //BaseAction baseAction = baseActionMapper.selectOne(baseActionQueryWrapper);
            UserTestActionDto userTestActionDto = baseActionMapper.getUserTestActionDto(e.getId());
            if(userTestActionDto != null) {
                userTestActionDto.setPicUrl(fileImagesPath+userTestActionDto.getPicUrl());
                userTestActionDto.setVideoUrl(fileVideoPath+userTestActionDto.getVideoUrl());
            }
            userPowerTestDto.setUserTestActionDto(userTestActionDto);
            userPowerTestDtos.add(userPowerTestDto);
        });
        return ResultUtil.success(userPowerTestDtos);
    }

    @Override
    public UserActionPowerTest insertReturnEntity(UserActionPowerTest entity) {
        return null;
    }

    @Override
    public UserActionPowerTest updateReturnEntity(UserActionPowerTest entity) {
        return null;
    }
}
