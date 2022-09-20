package com.knd.front.login.handler;

import com.knd.common.response.Result;
import com.knd.front.login.request.LoginRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zm
 */
@Component
public class LoginHandlerFactoryV2 {

    @Resource
    private ApplicationContext applicationContext;

    public Result excuteHandler(LoginRequest loginRequest) throws Exception {
        ILoginHandler iLoginHandler = applicationContext.getBean(loginRequest.getLoginTypeEnum().getClassName(), ILoginHandler.class);
        return iLoginHandler.login(loginRequest);
    }
}
