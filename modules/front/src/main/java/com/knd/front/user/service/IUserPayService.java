package com.knd.front.user.service;

import com.knd.common.response.Result;
import com.knd.front.entity.UserPay;
import com.knd.front.user.request.UserPayAddRequest;
import com.knd.front.user.request.UserPayCheckRequest;
import com.knd.front.user.request.UserPayEditRequest;
import com.knd.mybatis.SuperService;

public interface IUserPayService extends SuperService<UserPay> {

    Result add(UserPayAddRequest request);

    Result edit(UserPayEditRequest request);

    Result checkList(UserPayCheckRequest request);

    Result check(String userId,Integer type,String payId);
}
