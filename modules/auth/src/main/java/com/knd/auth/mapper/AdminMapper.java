package com.knd.auth.mapper;

import com.knd.auth.dto.AuthUrlDto;
import com.knd.auth.dto.PowerDto;
import com.knd.auth.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.auth.entity.Role;
import com.knd.auth.entity.RoleInfo;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-07-09
 */
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 权限集合列表
     * @return
     */
    List<AuthUrlDto> queryAuthUrlList(String userId);

    /**
     * 页面权限集合
     * @param userId
     * @return
     */
    List<PowerDto> selectPowerList(String userId);

    Set<String> selectRoleList(String userId);

    List<Role> selectRoleListNew(String userId);

    List<RoleInfo> selectRoleListInfo(String userId);
}
