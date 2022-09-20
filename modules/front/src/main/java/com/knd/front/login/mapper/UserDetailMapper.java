package com.knd.front.login.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.entity.UserDetail;
import com.knd.front.login.request.UserDetailRequest;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sy
 * @since 2020-07-01
 */

public interface UserDetailMapper extends BaseMapper<UserDetail> {

    int updateUserDetail(UserDetailRequest userDetailRequest);



}
