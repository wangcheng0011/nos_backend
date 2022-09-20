package com.knd.manage.user.service;

import com.knd.common.response.Result;
import com.knd.manage.user.entity.UserLoginInfo;
import com.knd.manage.user.request.LoginRequest;
import com.knd.mybatis.SuperService;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-07
 */

public interface IUserLoginInfoService extends SuperService<UserLoginInfo> {
    //查询注册会员登录列表
    Result queryUserLoginInfoList(String nickName, String mobile, String equipmentNo, String loginInTimeBegin,
                                  String loginInTimeEnd, String current) throws ParseException;

    void loginAccount(LoginRequest loginRequest);


    Result verifyCodeLogin(LoginRequest loginRequest) throws Exception;

    Result smallRoutineLogin(HttpServletResponse httpServletResponse, LoginRequest loginRequest) throws Exception;

    Result weiXinlogin(LoginRequest loginRequest) throws Exception;

    void wxcallback(String code, String state,HttpServletResponse httpServletResponse) throws Exception;

    Result wxLogin(String state);

    Result wxQQLoginPolling(String state) throws Exception;

    Result qqLogin(String state) throws UnsupportedEncodingException;

    void qqcallback(String code, String state,HttpServletResponse httpServletResponse) throws Exception;
}
