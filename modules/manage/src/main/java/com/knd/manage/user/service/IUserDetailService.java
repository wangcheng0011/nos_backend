package com.knd.manage.user.service;

import com.knd.common.response.Result;
import com.knd.manage.user.entity.UserDetail;
import com.knd.manage.user.request.UserDetailRequest;
import com.knd.mybatis.SuperService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-07
 */
public interface IUserDetailService extends SuperService<UserDetail> {
    /**
     * 维护用户明细数据
     * @param userDetailRequest
     * @return
     */
    Result addOrUpdateUserDetail(UserDetailRequest userDetailRequest);
    //查询注册会员详情
    Result getUserDetail(String userId);

    String getHeadUrl(String userId);

}
