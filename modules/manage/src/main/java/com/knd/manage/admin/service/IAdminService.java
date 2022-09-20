package com.knd.manage.admin.service;

import com.knd.common.response.Result;
import com.knd.manage.admin.entity.Admin;
import com.knd.manage.admin.vo.VoSaveAdmin;
import com.knd.manage.admin.vo.VoSaveFrozenFlag;
import com.knd.manage.common.vo.VoId;
import com.knd.mybatis.SuperService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-09
 */
public interface IAdminService extends SuperService<Admin> {

    Result insertAdmin(Admin admin);

    Result updateAdmin(Admin admin);

    //新增
    Result add(VoSaveAdmin vo);

    //更新
    Result edit(VoSaveAdmin vo);

    //更新用户冻结状态
    Result saveFrozenFlag(VoSaveFrozenFlag vo);

    //删除用户
    Result deleteAdmin(VoId vo);

    //查询用户
    Result getAdmin(String id);

    //查询用户列表
    Result getAdminList(String userName, String nickName, String mobile, String frozenFlag, String current);


    Result updateMaintenanceWorkerPassword(String userId);
}
