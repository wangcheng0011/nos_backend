package com.knd.manage.user.service.impl;

import com.knd.manage.user.entity.UserTrainLevel;
import com.knd.manage.user.mapper.UserTrainLevelMapper;
import com.knd.manage.user.service.IUserTrainLevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-15
 */
@Service
public class UserTrainLevelServiceImpl extends ServiceImpl<UserTrainLevelMapper, UserTrainLevel> implements IUserTrainLevelService {

    @Override
    public UserTrainLevel insertReturnEntity(UserTrainLevel entity) {
        return null;
    }

    @Override
    public UserTrainLevel updateReturnEntity(UserTrainLevel entity) {
        return null;
    }
}
