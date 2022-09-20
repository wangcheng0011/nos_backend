package com.knd.manage.user.service;

import com.knd.common.response.Result;
import com.knd.manage.user.entity.IntroductionCourse;
import com.knd.mybatis.SuperService;

import java.text.ParseException;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-08
 */
public interface IIntroductionCourseService extends SuperService<IntroductionCourse> {
    //查询注册会员课程列表
    Result queryUserCourseList(String nickName, String mobile, String equipmentNo, String trainTimeBegin,
                               String trainTimeEnd, String current) throws ParseException;

    Result queryCourseList(String current);
}
