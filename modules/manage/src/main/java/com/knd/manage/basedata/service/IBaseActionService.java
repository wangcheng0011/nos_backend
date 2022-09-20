package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.entity.BaseAction;
import com.knd.manage.basedata.vo.VoSaveAction;
import com.knd.manage.common.vo.VoId;
import com.knd.mybatis.SuperService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-06-30
 */
public interface IBaseActionService extends SuperService<BaseAction> {
    //新增动作
    Result add(VoSaveAction vo );

    //更新动作
    Result edit(VoSaveAction vo );

    //删除动作
    Result deleteAction(VoId vo);

    //获取动作
    Result getAction(String actionId);

    //获取动作列表
    Result getActionList(String actionType,String target, String part, String action, String currentPage);
}
