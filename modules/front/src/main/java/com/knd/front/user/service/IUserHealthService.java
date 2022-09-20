package com.knd.front.user.service;

import com.knd.common.response.Result;
import com.knd.front.user.entity.UserHealthEntity;
import com.knd.front.user.request.UserHealthRequest;
import com.knd.mybatis.SuperService;

import java.time.LocalDate;

public interface IUserHealthService {

    Result addOrUpdate(String userId, UserHealthRequest vo);

    Result getHealth(String userId);

    Result getHealthByDate(String userId,LocalDate beginDate,LocalDate endDate);
}
