package com.knd.batch.service.impl;

import com.knd.batch.entity.User;
import com.knd.batch.mapper.UserMapper;
import com.knd.batch.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-08-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User insertReturnEntity(User entity) {
        return null;
    }

    @Override
    public User updateReturnEntity(User entity) {
        return null;
    }
}
