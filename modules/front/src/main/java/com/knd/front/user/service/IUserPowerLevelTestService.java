package com.knd.front.user.service;

import com.knd.common.response.Result;
import com.knd.front.entity.UserPowerLevelTest;
import com.knd.mybatis.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llx
 * @since 2020-07-03
 */
public interface IUserPowerLevelTestService extends SuperService<UserPowerLevelTest> {
   Result getUserPowerLevels(String userId);
}
