package com.knd.front.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.common.response.Result;
import com.knd.common.response.ResultUtil;
import com.knd.front.entity.UserPowerLevelTest;

import com.knd.front.user.mapper.UserPowerLevelTestMapper;
import com.knd.front.user.service.IUserPowerLevelTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-07-03
 */
@Service
public class UserPowerLevelTestServiceImpl extends ServiceImpl<UserPowerLevelTestMapper, UserPowerLevelTest> implements IUserPowerLevelTestService {
    @Autowired
    private UserPowerLevelTestMapper userPowerLevelTestMapper;
    @Override
    public UserPowerLevelTest insertReturnEntity(UserPowerLevelTest entity) {
        return null;
    }

    @Override
    public UserPowerLevelTest updateReturnEntity(UserPowerLevelTest entity) {
        return null;
    }

    @Override
    public Result getUserPowerLevels(String userId) {
        return ResultUtil.success(userPowerLevelTestMapper.getUserPowerLevels(userId));
    }
}
