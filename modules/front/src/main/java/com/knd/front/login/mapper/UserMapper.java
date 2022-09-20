package com.knd.front.login.mapper;

import com.knd.front.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.knd.front.login.request.LoginRequest;
import com.knd.front.login.request.ResetRequest;
import com.knd.front.login.request.UserLoginInfoRequest;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
public interface UserMapper extends BaseMapper<User> {
    int updateReset(ResetRequest resetRequest);
}
