package com.knd.manage.user.service;

import com.knd.common.log.Log;
import com.knd.common.response.Result;
import com.knd.manage.user.entity.UserPowerLevelTest;
import com.knd.mybatis.SuperService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.ParseException;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-27
 */
public interface IUserPowerLevelTestService extends SuperService<UserPowerLevelTest> {

    //查询注册会员力量等级测试列表
    Result queryPowerLevelTestList(String nickName, String mobile, String action, String trainTimeBegin,
                                   String trainTimeEnd, String current) throws ParseException;
}
