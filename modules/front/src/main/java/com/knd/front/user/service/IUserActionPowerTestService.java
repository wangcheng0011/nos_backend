package com.knd.front.user.service;

import com.knd.common.response.Result;
import com.knd.front.entity.UserActionPowerTest;
import com.knd.front.user.request.UserActionPowerTestRequest;
import com.knd.mybatis.SuperService;

import java.util.List;

/**
 * @author will
 */
public interface IUserActionPowerTestService extends SuperService<UserActionPowerTest> {
    String getUserTrainPower(String userId,String actionType);

    Result saveUserTranPower(List<UserActionPowerTestRequest> userActionPowerTestRequest);

    Result getActionPowerTestResult(String userId);

    Result getActionPowerTestList(String userId);
}
