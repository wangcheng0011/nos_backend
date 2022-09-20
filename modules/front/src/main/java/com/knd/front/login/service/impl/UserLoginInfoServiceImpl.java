package com.knd.front.login.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.knd.front.entity.UserLoginInfo;
import com.knd.front.login.mapper.UserLoginInfoMapper;
import com.knd.front.login.service.IUserLoginInfoService;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author llx
 * @since 2020-07-06
 */
@Service
public class UserLoginInfoServiceImpl extends ServiceImpl<UserLoginInfoMapper, UserLoginInfo> implements IUserLoginInfoService {

    @Override
    public UserLoginInfo insertReturnEntity(UserLoginInfo entity) {
        return null;
    }

    @Override
    public UserLoginInfo updateReturnEntity(UserLoginInfo entity) {
        return null;
    }
}
