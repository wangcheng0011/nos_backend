package com.knd.manage.admin.service;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.manage.admin.entity.Role;
import com.knd.manage.admin.vo.VoSaveRole;
import com.knd.manage.admin.vo.VoSaveRolePower;
import com.knd.manage.common.vo.VoId;
import com.knd.mybatis.SuperService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-17
 */
public interface IRoleService extends SuperService<Role> {
    //新增角色信息
    Result add(VoSaveRole vo);

    //编辑角色信息
    Result edit(VoSaveRole vo);

    //删除角色
    Result deleteRole(VoId vo);

    //查询角色信息
    Result getRole(String id);

    //查询角色列表
    Result getRoleList(String name, String current);

    //维护角色权限
    Result saveRolePower(VoSaveRolePower vo);

}
