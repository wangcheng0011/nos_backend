package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.entity.BaseTarget;
import com.knd.mybatis.SuperService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface IBaseTargetService extends SuperService<BaseTarget> {
    //新增目标
    Result add(String userId, String target, String remark);

    //更新目标
    Result edit(String userId, String target, String remark, String targetId);

    //删除目标
    Result deleteTarget(String userId, String id);

    //获取目标
    Result getTarget(String id);

    //获取目标列表
    Result getTargetList(String target, String currentPage);

}
