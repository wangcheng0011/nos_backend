package com.knd.front.train.service;

import com.knd.common.response.Result;
import com.knd.front.entity.ActionArray;
import com.knd.front.train.request.GetActionArrayRequest;
import com.knd.front.train.request.SaveActionArrayRequest;
import com.knd.front.train.request.UpdateActionArrayRequest;
import com.knd.mybatis.SuperService;


/**
 * @author will
 */
public interface IActionArrayService extends SuperService<ActionArray> {
    Result saveActionArray(SaveActionArrayRequest saveActionArrayRequest);

    Result updateActionArray(UpdateActionArrayRequest saveActionArrayRequest);

    Result getUserActionArray(GetActionArrayRequest getActionArrayRequest);

    Result getUserActionArrayInfo(String userId,String actionArrayId);

    Result deleteUserActionArray(String userId, String actionArrayId);
}
