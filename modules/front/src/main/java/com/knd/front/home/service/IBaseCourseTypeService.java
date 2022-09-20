package com.knd.front.home.service;

import com.knd.common.response.Result;
import com.knd.front.entity.BaseCourseType;

import com.knd.front.home.dto.*;
import com.knd.mybatis.SuperService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author llx
 * @since 2020-07-01
 */
public interface IBaseCourseTypeService extends SuperService<BaseCourseType> {
    Result getHomeCourseList(String userId);

    Result getFilterCourseLabelSettings(String userId);


}
