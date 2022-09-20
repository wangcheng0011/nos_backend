package com.knd.front.user.service;

import com.knd.common.response.Result;

public interface IUserPurchasedService {

    Result getPurchasedCourse(String userId,String currentPage);
}
