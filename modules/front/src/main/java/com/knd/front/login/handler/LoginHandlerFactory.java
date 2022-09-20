package com.knd.front.login.handler;

import com.knd.common.em.LoginTypeEnum;
import com.knd.common.response.Result;
import com.knd.front.login.request.LoginRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author will
 * @date 2021年04月30日 14:36
 */
@Component
public class LoginHandlerFactory implements ApplicationContextAware {

    private ApplicationContext applicationContext;


    private static Map<LoginTypeEnum,ILoginHandler> loginHandlerContext = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        registLoginHandler();
    }

    private void registLoginHandler() {
        loginHandlerContext.put(LoginTypeEnum.LOGIN_PASSWORD,
                applicationContext.getBean("loginPasswordHandler",ILoginHandler.class));
        loginHandlerContext.put(LoginTypeEnum.LOGIN_MOBILE,
                applicationContext.getBean("loginMobileHandler",ILoginHandler.class));
        loginHandlerContext.put(LoginTypeEnum.LOGIN_VERIFYCODE,
                applicationContext.getBean("loginVerifyCodeHandler",ILoginHandler.class));
        loginHandlerContext.put(LoginTypeEnum.LOGIN_APPLE,
                applicationContext.getBean("loginAppleHandler",ILoginHandler.class));
    }

    public static Result excuteHandler(LoginTypeEnum loginTypeEnum,LoginRequest loginRequest) throws Exception {
        ILoginHandler iLoginHandler = loginHandlerContext.get(loginTypeEnum);
        return iLoginHandler.login(loginRequest);
    }
}
