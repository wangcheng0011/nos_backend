package com.knd.front.login.service;

import com.knd.common.response.Result;
import com.knd.front.entity.User;
import com.knd.front.login.request.*;
import com.knd.mybatis.SuperService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author llx
 * @since 2020-06-30
 */
public interface IUserService extends SuperService<User> {

    /**
     * 用户注册
     * @param registerRequest
     * @return
     */
    Result registerUser(RegisterRequest registerRequest);

    int resetPassword(ResetRequest resetRequest, List existCode);

    Result changeVipType(ChangeVipTypeRequest changeVipTypeRequest);

    Result checkBeforeBindingSecondary(BindingSecondaryRequest bindingSecondaryRequest);

    Result bindingSecondary(BindingSecondaryRequest bindingSecondaryRequest);

    Result unBindingSecondary(BindingSecondaryRequest bindingSecondaryRequest);

    Result getBindingSecondaryList(getBindingSecondaryListRequest getBindingSecondaryListRequest);
    
    Result getShareAndHobby();

    /**
     * 获取运动方式与运动频率
     * @return
     */
    Result getSportAndFrequency();

    /**
     * 获取配件
     * @return
     */
    Result getEquipmentList();

 //   void loginAccount(LoginRequest loginRequest);

   // User registerUser2(RegisterRequest registerRequest);

    /**
     * 更新用户
     * @param user
     */
    void updateUser(User user);

    /**
     * 查询手机号注册用户信息
     * @param mobile
     * @return
     */
    User getUserByMobile(String mobile);

    User registerUser2(RegisterRequest registerRequest);

    Result logout(LogoutRequest logoutRequest);

    User createVirtualAccount(String userId);
}
