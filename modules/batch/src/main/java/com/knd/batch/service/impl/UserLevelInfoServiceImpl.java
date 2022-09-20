package com.knd.batch.service.impl;

import com.knd.batch.entity.UserLevelInfo;
import com.knd.batch.mapper.UserLevelInfoMapper;
import com.knd.batch.service.IUserLevelInfoService;
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
public class UserLevelInfoServiceImpl extends ServiceImpl<UserLevelInfoMapper, UserLevelInfo> implements IUserLevelInfoService {

    @Override
    public UserLevelInfo insertReturnEntity(UserLevelInfo entity) {
        return null;
    }

    @Override
    public UserLevelInfo updateReturnEntity(UserLevelInfo entity) {
        return null;
    }
}
