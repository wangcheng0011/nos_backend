package com.knd.manage.basedata.service;

import com.knd.common.response.Result;
import com.knd.manage.basedata.entity.ActionType;
import com.knd.mybatis.SuperService;

/**
 * @author will
 */
public interface IBaseActionTypeService extends SuperService<ActionType> {


    //获取动作类型列表
    Result getActionTypeList();

}
