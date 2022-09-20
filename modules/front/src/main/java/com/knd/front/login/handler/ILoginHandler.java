package com.knd.front.login.handler;

import com.knd.common.response.Result;
import com.knd.front.login.request.LoginRequest;

/**
 * @author will
 * @date 2021年04月30日 14:32
 */
public interface ILoginHandler {
    Result login(LoginRequest loginRequest) throws Exception;
}
