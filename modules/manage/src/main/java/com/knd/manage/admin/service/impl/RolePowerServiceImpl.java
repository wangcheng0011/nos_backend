package com.knd.manage.admin.service.impl;

import com.knd.manage.admin.entity.RolePower;
import com.knd.manage.admin.mapper.RolePowerMapper;
import com.knd.manage.admin.service.IRolePowerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-17
 */
@Service
public class RolePowerServiceImpl extends ServiceImpl<RolePowerMapper, RolePower> implements IRolePowerService {

    @Override
    public RolePower insertReturnEntity(RolePower entity) {
        return null;
    }

    @Override
    public RolePower updateReturnEntity(RolePower entity) {
        return null;
    }
}
