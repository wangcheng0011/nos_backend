package com.knd.manage.admin.mapper;

import com.knd.manage.admin.entity.AdminRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-07-19
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {
    //根据用户id获取关联的角色名称
    List<String> selectNamelist(String id);

    //根据角色id查看绑定的用户数量
    int selectCountByAdminId(String roleId);
}
