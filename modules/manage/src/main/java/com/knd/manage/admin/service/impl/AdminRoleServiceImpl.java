package com.knd.manage.admin.service.impl;

import com.knd.manage.admin.entity.AdminRole;
import com.knd.manage.admin.mapper.AdminRoleMapper;
import com.knd.manage.admin.service.IAdminRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sy
 * @since 2020-07-19
 */
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements IAdminRoleService {

    @Override
    public AdminRole insertReturnEntity(AdminRole entity) {
        return null;
    }

    @Override
    public AdminRole updateReturnEntity(AdminRole entity) {
        return null;
    }
}
