package com.knd.manage.course.service;

import com.knd.common.response.Result;
import com.knd.manage.course.entity.BaseCourseType;
import com.knd.mybatis.SuperService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author sy
 * @since 2020-07-01
 */
public interface IBaseCourseTypeService extends SuperService<BaseCourseType> {

    //新增课程分类
    Result add(String userId, String type, String remark, String appHomeFlag, String sort);

    //更新课程分类
    Result edit(String userId, String type, String remark, String appHomeFlag, String sort, String typeId);

    //删除课程分类
    Result deleteCourseType(String userId, String id);

    //获取课程分类
    Result getCourseType(String id);

    //获取课程分类列表
    Result getCourseTypeList(String type, String currentPage);
}
