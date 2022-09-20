package com.knd.manage.user.service;

import com.knd.common.response.Result;
import com.knd.manage.user.entity.User;
import com.knd.manage.user.request.RegisterRequest;
import com.knd.manage.user.vo.*;
import com.knd.mybatis.SuperService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-07
 */
public interface IUserService extends SuperService<User> {
    //查询注册会员列表
    Result queryRegistedUserList(String nickName, String mobile, String frozenFlag, String registTimeBegin,
                                 String registTimeEnd, String currentPage) throws ParseException;

    //查询注册会员训练列表
    Result queryUserTrainList(String nickName, String mobile, String equipmentNo, String trainTimeBegin,
                              String trainTimeEnd, String current,String trainType) throws ParseException;

    //查询注册会员训练详情
    Result queryUserTrainInfo(VoQueryUserTrainInfo vo);

    //修改注册会员状态
    Result saveFrozenFlag(String userId, VoSaveFrozenFlag vo);

    Result deleteUser(VoDeleteUser vo);

    User registerUser2(RegisterRequest registerRequest) throws Exception;

    /**
     * 查询手机号注册用户信息
     * @param mobile
     * @return
     */
    User getUserByMobile(String mobile);

    /**
     * 保存用户微信信息
     * @param
     * @return
     */

    Result updateUserWxInfo(VoSaveUserWxInfo vo);

    Result uploadWxPicture(VoSaveUserWxPicInfo vo, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
