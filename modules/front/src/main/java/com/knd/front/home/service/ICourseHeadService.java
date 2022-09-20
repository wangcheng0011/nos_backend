package com.knd.front.home.service;

import com.knd.common.response.Result;
import com.knd.front.entity.CourseHead;

import com.knd.front.home.request.UserQueryCourseRequest;
import com.knd.mybatis.SuperService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author llx
 * @since 2020-07-01
 */
public interface ICourseHeadService extends SuperService<CourseHead> {

    Result getCourse(UserQueryCourseRequest request);

    Result getCoursePage(UserQueryCourseRequest request);

}
