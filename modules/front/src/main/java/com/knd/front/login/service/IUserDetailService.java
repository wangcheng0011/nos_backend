package com.knd.front.login.service;

import com.knd.common.response.Result;
import com.knd.front.entity.UserDetail;
import com.knd.front.login.request.UserDetailRequest;
import com.knd.mybatis.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-01
 */
public interface IUserDetailService extends SuperService<UserDetail> {
   /**
    * 维护用户明细数据
    * @param userDetailRequest
    * @return
    */
   Result addOrUpdateUserDetail(UserDetailRequest userDetailRequest);

   Result getUserDetail(String userId);

   String getHeadUrl(String userId);

   Result updateUserDetail(UserDetailRequest userDetailRequest);
}
