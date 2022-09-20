package com.knd.batch.service.impl;

import com.knd.batch.entity.UserTrainLevel;
import com.knd.batch.mapper.UserTrainLevelMapper;
import com.knd.batch.service.IUserTrainLevelService;
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
